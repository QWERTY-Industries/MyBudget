package qi.mybudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import qi.mybudget.databinding.ActivitySignUpscreenBinding
import qi.mybudget.databinding.FragmentCreateExpenseBinding
import qi.mybudget.databinding.FragmentHomeBinding

class CreateExpense : Fragment() {
    // Declare binding variables
    private var _binding: FragmentCreateExpenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentCreateExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Q Commented code is database not creating table and throw compile error

//        val db = Room.databaseBuilder(
//            requireContext().applicationContext,
//            AppDatabase::class.java, "database-new"
//        ).allowMainThreadQueries().build()
//
//        val expenseDao = db.expenseDao();
//        val expenses: List<Expense> = budgetDao.findExpensesByUserId();

        //Q Create
        binding.btnCreateExpense.setOnClickListener {
            findNavController().navigate(R.id.action_createExpense_to_homeFrag)
//            expenseDao.createExpense(Expense())
        }

        //Q Category
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createExpense_to_homeFrag)
        }

        // Note: Your "Side Menu" button with the id "button" is not yet used.
        // You can add a listener for it here if you need to.
        // binding.button.setOnClickListener { ... }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object when the view is destroyed to avoid memory leaks
        _binding = null
    }
}