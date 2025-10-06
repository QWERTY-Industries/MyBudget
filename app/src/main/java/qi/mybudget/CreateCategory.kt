package qi.mybudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import qi.mybudget.databinding.FragmentCreateCategoryBinding
import qi.mybudget.databinding.FragmentCreateExpenseBinding

class CreateCategory : Fragment() {
    // Declare binding variables
    private var _binding: FragmentCreateCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
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
//        val categoryDao = db.categoryDao();
//        val categories: List<Category> = categoryDao.findCategoriesByUserId();

        //Q Create
        binding.btnCreateCategory.setOnClickListener {
            findNavController().navigate(R.id.action_createCategory_to_homeFrag)
//            categoryDao.createCategory(Category(0, 0, binding.etName.text.toString()))
        }

        //Q Back
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createCategory_to_homeFrag)
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