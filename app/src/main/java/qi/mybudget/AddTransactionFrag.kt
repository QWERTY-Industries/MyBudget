package qi.mybudget

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
// --- NEW --- Import the ViewModel and the activityViewModels delegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import qi.mybudget.databinding.FragmentAddTransactionBinding

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    // --- NEW --- Get the shared ViewModel instance
    // This is the key to connecting this fragment with the others.
    private val viewModel: AnalysisViewModel by activityViewModels()

    // We still need these for fetching categories, which is a UI-only concern for this screen.
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val categoryList = mutableListOf<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupCategorySpinner()
        fetchCategories()

        binding.btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }

        binding.btnSaveTransaction.setOnClickListener {
            // --- MODIFIED --- This now calls a ViewModel function
            saveTransaction()
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    // --- MODIFIED: The save logic now delegates to the ViewModel ---
    private fun saveTransaction() {
        val amountStr = binding.etAmount.text.toString()
        val category = if (binding.spinnerCategory.selectedItem != null) {
            binding.spinnerCategory.selectedItem.toString()
        } else ""
        val description = binding.etDescription.text.toString().trim()
        val transactionType = if (binding.rbIncome.isChecked) "Income" else "Expense"

        // --- NEW --- Get the selected payment source from the new RadioGroup
        val paymentSource = when (binding.rgPaymentSource.checkedRadioButtonId) {
            R.id.rbSourceCard -> "Card"
            R.id.rbSourceSavings -> "Savings"
            else -> "Cash" // Default to Cash
        }

        // Validation remains the same...
        if (amountStr.isBlank() || category.isBlank()) {
            Toast.makeText(context, "Amount and Category cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        // --- MODIFIED --- Pass the new 'paymentSource' to the ViewModel function
        viewModel.saveTransaction(transactionType, category, description, amount, paymentSource)

        Toast.makeText(context, "Transaction saved!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }


    // --- UNCHANGED --- The rest of the code for category management remains the same ---

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Category")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        input.hint = "e.g., Shopping"
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val categoryName = input.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                saveNewCategory(categoryName)
            } else {
                Toast.makeText(context, "Category name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun saveNewCategory(categoryName: String) {
        val currentUser = auth.currentUser ?: return
        val categoryRef = database.child("categories").child(currentUser.uid)
        val categoryId = categoryRef.push().key ?: return

        val newCategory = Category(
            categoryId = categoryId,
            name = categoryName,
            userId = currentUser.uid
        )

        categoryRef.child(categoryId).setValue(newCategory)
            .addOnSuccessListener {
                Toast.makeText(context, "Category '$categoryName' saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save category: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun setupCategorySpinner() {
        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
    }

    private fun fetchCategories() {
        val currentUser = auth.currentUser ?: return
        val defaultCategories = listOf("Groceries", "Transport", "Entertainment", "Utilities", "Health")
        val userCategoriesRef = database.child("categories").child(currentUser.uid)

        userCategoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                categoryList.addAll(defaultCategories)

                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    category?.name?.let {
                        if (!categoryList.contains(it)) { categoryList.add(it) }
                    }
                }
                categoryList.sort()
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
