package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import qi.mybudget.databinding.FragmentWalletBinding
import java.text.NumberFormat
import java.util.Locale

class WalletFrag : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var currentWallet: Wallet? = null

    // This formatter will add the currency symbol based on the user's locale (e.g., $, £, €)
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }
        database = FirebaseDatabase.getInstance().reference.child("wallets").child(userId)

        setupClickListeners()
        loadWalletData()
    }

    private fun setupClickListeners() {
        binding.btnUpdateWallet.setOnClickListener {
            saveWalletData()
        }

        binding.btnWalletBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadWalletData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Deserialize the data into our Wallet data class
                currentWallet = snapshot.getValue(Wallet::class.java)

                // If wallet is null (user's first time), create a default one
                if (currentWallet == null) {
                    currentWallet = Wallet(0.0, 0.0, 0.0)
                }

                // Update the UI with the fetched data
                updateUI(currentWallet)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load wallet: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * This function now handles displaying the formatted currency.
     * It shows the hint if the value is 0.
     */
    private fun updateUI(wallet: Wallet?) {
        wallet ?: return

        // For Cash Balance
        if (wallet.cashBalance != null && wallet.cashBalance > 0) {
            binding.etCashBalance.setText(wallet.cashBalance.toString())
        } else {
            binding.etCashBalance.setText("") // Clear the text to show the hint
        }

        // For Card Balance
        if (wallet.cardBalance != null && wallet.cardBalance > 0) {
            binding.etCardBalance.setText(wallet.cardBalance.toString())
        } else {
            binding.etCardBalance.setText("")
        }

        // For Savings Balance
        if (wallet.savingsBalance != null && wallet.savingsBalance > 0) {
            binding.etSavingsBalance.setText(wallet.savingsBalance.toString())
        } else {
            binding.etSavingsBalance.setText("")
        }

        // --- NEW --- Add a TextView to display the total formatted with currency
        val totalBalance = (wallet.cashBalance ?: 0.0) + (wallet.cardBalance ?: 0.0) + (wallet.savingsBalance ?: 0.0)
        binding.tvTotalBalance.text = "Total: ${currencyFormatter.format(totalBalance)}"
    }

    private fun saveWalletData() {
        // Parse the text from EditTexts, defaulting to 0.0 if empty or invalid
        val cash = binding.etCashBalance.text.toString().toDoubleOrNull() ?: 0.0
        val card = binding.etCardBalance.text.toString().toDoubleOrNull() ?: 0.0
        val savings = binding.etSavingsBalance.text.toString().toDoubleOrNull() ?: 0.0

        val updatedWallet = Wallet(cash, card, savings)

        database.setValue(updatedWallet)
            .addOnSuccessListener {
                Toast.makeText(context, "Wallet updated successfully!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update wallet: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}