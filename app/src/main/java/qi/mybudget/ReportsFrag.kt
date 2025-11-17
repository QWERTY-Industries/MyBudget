package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Use activityViewModels to share with AnalysisFrag
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import qi.mybudget.databinding.FragmentReportsBinding // Use the correct binding class name

class ReportsFrag : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    // Use activityViewModels to get the SAME instance of the ViewModel as AnalysisFrag and other fragments.
    private val viewModel: AnalysisViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView with a layout manager
        binding.recyclerViewReports.layoutManager = LinearLayoutManager(context)

        // Observe the transactions LiveData from the shared ViewModel
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            // Whenever the data changes, update the RecyclerView.
            // Sort the list by date descending, so the newest transactions are at the top.
            val sortedList = transactions.sortedByDescending { it.date }
            binding.recyclerViewReports.adapter = TransactionAdapter(sortedList)
        }

        // Set up the back button
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}