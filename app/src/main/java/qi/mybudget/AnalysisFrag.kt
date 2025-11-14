package qi.mybudget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.compose.ui.tooling.data.position
//import androidx.core.text.color
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
// Corrected MPAndroidChart imports
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
// Corrected standard Java/Android imports
import qi.mybudget.databinding.FragmentAnalysisBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AnalysisFrag : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expenditureData = getSampleExpenditureData()
        setupLineChart(expenditureData)
    }

    private fun setupLineChart(data: List<Pair<Long, Float>>) {
        val entries = ArrayList<Entry>()
        // The 'forEach' loop is a standard Kotlin function and doesn't need a special import.
        data.forEach { pair ->
            // 'toFloat()' and 'pair.second' are also standard and do not need imports.
            entries.add(Entry(pair.first.toFloat(), pair.second))
        }

        val dataSet = LineDataSet(entries, "Expenditure")

        dataSet.color = Color.BLACK
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(Color.BLACK)
        dataSet.setDrawValues(false)
        dataSet.lineWidth = 2.5f
        dataSet.circleRadius = 4f

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData

        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.textColor = Color.DKGRAY

        val xAxis = binding.lineChart.xAxis
        xAxis.textColor = Color.DKGRAY
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = TimeUnit.DAYS.toMillis(1).toFloat()

        xAxis.valueFormatter = object : ValueFormatter() {
            private val format = SimpleDateFormat("MMM dd", Locale.getDefault())

            // The return type should be the standard 'String', not 'kotlin.text.String'
            override fun getFormattedValue(value: Float): String {
                // 'toLong()' is a standard function.
                return format.format(Date(value.toLong()))
            }
        }

        binding.lineChart.axisLeft.textColor = Color.DKGRAY
        binding.lineChart.axisRight.isEnabled = false

        binding.lineChart.invalidate()
    }

    // Use the standard 'Long' and 'Float' types.
    //placeholder for the users input. replace this with actual user inputs
    private fun getSampleExpenditureData(): List<Pair<Long, Float>> {
        val dayInMillis = TimeUnit.DAYS.toMillis(1)
        val now = System.currentTimeMillis()
        return listOf(
            Pair(now - dayInMillis * 6, 50f),
            Pair(now - dayInMillis * 5, 75f),
            Pair(now - dayInMillis * 4, 60f),
            Pair(now - dayInMillis * 3, 120f),
            Pair(now - dayInMillis * 2, 90f),
            Pair(now - dayInMillis * 1, 150f),
            Pair(now, 110f)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Use the standard 'String' type here.
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnalysisFrag().apply {
                arguments = Bundle().apply {
                    // Left this in case you decide to use it later
                }
            }
    }
}