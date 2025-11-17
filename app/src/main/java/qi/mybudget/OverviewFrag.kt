package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import qi.mybudget.databinding.FragmentOverviewBinding
import java.text.NumberFormat
import java.util.Locale

class OverviewFrag : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    // Get the shared ViewModel instance
    private val viewModel: AnalysisViewModel by activityViewModels()

    // Create a currency formatter for Rands
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the back button listener
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Start observing the data from the ViewModel
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.wallet.observe(viewLifecycleOwner) { wallet ->
            // Use a default, empty Wallet object if the observed value is null
            val currentWallet = wallet ?: Wallet()

            val totalBalance = (currentWallet.cashBalance ?: 0.0) +
                    (currentWallet.cardBalance ?: 0.0) +
                    (currentWallet.savingsBalance ?: 0.0)

            binding.tvCurrentBalance.text = "Balance: ${currencyFormatter.format(totalBalance)}"
        }

        // --- OBSERVE TOTAL SPENT (This is the missing piece) ---
        viewModel.monthlyExpenses.observe(viewLifecycleOwner) { totalSpent ->
            val formattedAmount = currencyFormatter.format(totalSpent ?: 0.0)
            binding.tvTotalSpent.text = "You Spent: ${formattedAmount}"
        }

        // --- OBSERVE TOTAL INCOME (This is the other missing piece) ---
        viewModel.monthlyIncome.observe(viewLifecycleOwner) { totalIncome ->
            val formattedAmount = currencyFormatter.format(totalIncome ?: 0.0)
            binding.tvTotalIncome.text = "Your Income: ${formattedAmount}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
