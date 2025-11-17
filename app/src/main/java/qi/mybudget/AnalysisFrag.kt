package qi.mybudget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import qi.mybudget.databinding.FragmentAnalysisBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AnalysisFrag : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    // Get the shared ViewModel instance that holds all our data
    private val viewModel: AnalysisViewModel by activityViewModels()

    // Currency formatter for Rands
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The RecyclerView is not needed for the graph, so we can hide it for now
       // binding.rvAnalysisTable.visibility = View.GONE

        setupChart()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        observeViewModelData()
    }

    private fun observeViewModelData() {
        // --- Observe Total Income ---
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvTotalIncome.text = "Income:\n${currencyFormatter.format(income ?: 0.0)}"
        }

        // --- Observe Total Expenses ---
        viewModel.totalExpenses.observe(viewLifecycleOwner) { expenses ->
            binding.tvTotalExpense.text = "Expenses:\n${currencyFormatter.format(expenses ?: 0.0)}"
        }

        // --- Observe Combined Totals for the "Total" TextView ---
        viewModel.totalIncome.observe(viewLifecycleOwner) { updateTotalAmount() }
        viewModel.totalExpenses.observe(viewLifecycleOwner) { updateTotalAmount() }


        // --- Observe the full transaction list to update the chart ---
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isNotEmpty()) {
                updateChartData(transactions)
            }
        }
    }

    private fun updateTotalAmount() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expenses = viewModel.totalExpenses.value ?: 0.0
        val total = income - expenses
        binding.tvTotalAmount.text = "Total:\n${currencyFormatter.format(total)}"
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            legend.textColor = Color.WHITE

            // Style the X-axis (bottom)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = DateAxisValueFormatter() // Use a custom formatter for dates

            // Style the Y-axis (left)
            axisLeft.textColor = Color.WHITE
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.argb(30, 255, 255, 255) // Faint white grid lines

            // Disable the right Y-axis
            axisRight.isEnabled = false
        }
    }

    private fun updateChartData(transactions: List<Transaction>) {
        // Group transactions by day and sum the amounts
        val dailyTotals = transactions
            .sortedBy { it.date }
            .groupBy {
                // Normalize date to the start of the day
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date ?: 0
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.timeInMillis
            }
            .mapValues { entry ->
                entry.value.sumOf {
                    if (it.transactionType == "Expense") -(it.amount ?: 0.0) else (it.amount ?: 0.0)
                }
            }

        // Create a running balance
        var runningBalance = 0.0
        val entries = dailyTotals.map { (date, totalChange) ->
            runningBalance += totalChange
            // The X value is the date (as a float), Y is the running balance
            Entry(date.toFloat(), runningBalance.toFloat())
        }

        if (entries.isEmpty()) {
            binding.lineChart.clear()
            return
        }

        val dataSet = LineDataSet(entries, "Account Balance Over Time")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.purple_chart)
        dataSet.valueTextColor = Color.WHITE
        dataSet.setCircleColor(Color.WHITE)
        dataSet.circleHoleColor = ContextCompat.getColor(requireContext(), R.color.purple_chart)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate() // Refresh the chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Helper class to format the X-axis labels from milliseconds to a "dd/MM" date format
class DateAxisValueFormatter : ValueFormatter() {
    private val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
    override fun getAxisLabel(value: Float, axis: com.github.mikephil.charting.components.AxisBase?): String {
        return sdf.format(Date(value.toLong()))
    }
}
