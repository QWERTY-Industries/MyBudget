package qi.mybudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import qi.mybudget.databinding.FragmentCreateBudgetBinding
import qi.mybudget.databinding.FragmentCreateExpenseBinding

class CreateBudget : Fragment() {
    // Declare binding variables
    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentCreateBudgetBinding.inflate(inflater, container, false)
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
//        val budgetDao = db.budgetDao();
//        val budgets: List<Budget> = budgetDao.findCategoriesByUserId();

        //Q Create
        binding.btnCreateBudget.setOnClickListener {
            findNavController().navigate(R.id.action_createBudget_to_homeFrag)
//            budgetDao.createBudget(Budget())
        }

        //Q Category
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createBudget_to_homeFrag)
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