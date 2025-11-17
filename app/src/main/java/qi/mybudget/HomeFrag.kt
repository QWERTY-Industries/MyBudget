package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import qi.mybudget.databinding.FragmentHomeBinding

class HomeFrag : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Get the SAME shared ViewModel instance that other fragments are using
    private val viewModel: AnalysisViewModel by activityViewModels()


    // Declare the adapter
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with an empty list
        transactionAdapter = TransactionAdapter(emptyList())
        binding.reportsRecyclerView.adapter = transactionAdapter
        binding.reportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupClickListeners() {
        // Add navigation for your buttons
        // Category
        binding.btnAnalysis.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_analysisFrag2)
        }

        // Budget
        binding.btnReporter.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_reporterFrag)
        }

        // Expense
        binding.btnOverview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_walletFrag)
        }
        // You'll need to set up the side menu click listener if it's not handled by MainActivity
    }

    private fun observeViewModel() {
        // This is where the magic happens!
        // Observe the transactions LiveData from the ViewModel
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            // When the list of transactions changes, update the UI

            // Check if the list is empty to show/hide the "empty" message
            if (transactions.isEmpty()) {
                binding.reportsRecyclerView.isVisible = false
                binding.tvEmptyReports.isVisible = true
            } else {
                binding.reportsRecyclerView.isVisible = true
                binding.tvEmptyReports.isVisible = false

                // Update the adapter with the new, sorted data (most recent first)
                transactionAdapter.updateData(transactions.sortedByDescending { it.date })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
