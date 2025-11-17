package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import qi.mybudget.databinding.FragmentCreateBudgetBinding

class CreateBudgetFrag : Fragment() {

    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!

    // Get the shared ViewModel
    private val viewModel: AnalysisViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make sure the input types are correct for numbers
        binding.etMinLimit.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        binding.etMaxLimit.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

        binding.btnCreateBudget.setOnClickListener {
            saveBudget()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveBudget() {
        val categoryName = binding.etName.text.toString().trim()
        val minLimitStr = binding.etMinLimit.text.toString()
        val maxLimitStr = binding.etMaxLimit.text.toString()

        // --- Input Validation ---
        if (categoryName.isEmpty()) {
            Toast.makeText(context, "Budget Name (Category) cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert strings to nullable Doubles. Invalid numbers become null.
        val minLimit = minLimitStr.toDoubleOrNull()
        val maxLimit = maxLimitStr.toDoubleOrNull()

        if (minLimitStr.isNotEmpty() && minLimit == null) {
            Toast.makeText(context, "Please enter a valid number for Min Limit.", Toast.LENGTH_SHORT).show()
            return
        }
        if (maxLimitStr.isNotEmpty() && maxLimit == null) {
            Toast.makeText(context, "Please enter a valid number for Max Limit.", Toast.LENGTH_SHORT).show()
            return
        }

        // Call the ViewModel to do the heavy lifting
        viewModel.createBudget(categoryName, minLimit, maxLimit)

        Toast.makeText(context, "Budget for '$categoryName' created!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}