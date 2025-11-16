package qi.mybudget

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
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

        //Q Initialise database
        val database = Firebase.database

        //Q Set the string value to database key value name
        //Q Use key values in read and write
        //Q This will work with same online database in all scripts BUT ensure database is initialised
        val myRef = database.getReference("expenses")

        //Q FIREBASE READ vvv

        // Read from the database
        //Q Look at my FIREBASE WRITE comments below before reading these read comments
        //Q Notice key value used here
        //Q Can also use child indenting when reading
        //Q Set frontend displays using values from the read
        myRef.child("myExpense").child("expenseName").get().addOnSuccessListener {
            //Q Make sure to call correct value the between the chevrons <>
            //Q In this case I am retrieving a string so I write .getValue<String>()
            //Q The value retrieved should be the expenseName of the myExpense object in the expenses table
            //Q This value is set to the string "testExpense" in FIREBASE WRITE below
            //Q Remember this value needs to be written before it is read or it will be null!!
            //Q Pay attention to call order!!
            //Q Put breakpoint here to see value that was written bellow
            var temp = it.getValue<String>()
        }

        //Q FIREBASE WRITE vvv

        //Q Create
        binding.btnCreateCategory.setOnClickListener {
            findNavController().navigate(R.id.action_createCategory_to_homeFrag)

            //Q Set value to user input
            //Q This expense object position 0 belongs to user object position 0 and category object position 0
            var testExpense = Expense(0, 0, 0, "testExpense", 20.0)

            //Q When create levels in database the child key values are put in the reference key value container
            //Q Can indent key values with child keyword as many times as needed

            //Q Look at my User, Budget, Category and Expense classes. Make each container with the reference
            //Q Then put objects inside by adding fields individually like this
            //Q Use primary keys to find objects and foreign keys to find connecting objects

            //Q uid is User ID, cid is Category ID, eid is Expense ID and bid is Budget ID
            //Q In this case I am creating an EXPENSE so EID is the primary key
            //Q While UID (Primary key of the user it is assigned to) and
            //Q Cid (Primary key of category it is assigned to) are the foreign keys

            //Q This code creates the following in the online firebase database
            //Q Expenses > MyExpense >
            //Q expenseEid = 0,
            //Q expenseUid = 0,
            //Q expenseCid = 0,
            //Q expenseName = "testExpense",
            //Q expenseAmount = 0

            myRef.child("myExpense").child("eid").setValue(testExpense.eid)
            myRef.child("myExpense").child("uid").setValue(testExpense.uid)
            myRef.child("myExpense").child("cid").setValue(testExpense.uid)
            myRef.child("myExpense").child("expenseName").setValue(testExpense.expenseName)
            myRef.child("myExpense").child("expenseAmount").setValue(testExpense.expenseAmount)

//            categoryDao.createCategory(Category(0, 0, binding.etName.text.toString()))
        }

        //Q Back
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_createCategory_to_homeFrag)
        }

        // Note: Your "Side Menu" button with the id "button" is not yet used.
        // You can add a listener for it here if you need to.
        // binding.button.setOnClickListener { ... }

        //Q Room db not creating table and throw compile error vvv

//        val db = Room.databaseBuilder(
//            requireContext().applicationContext,
//            AppDatabase::class.java, "database-new"
//        ).allowMainThreadQueries().build()
//
//        val categoryDao = db.categoryDao();
//        val categories: List<Category> = categoryDao.findCategoriesByUserId();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object when the view is destroyed to avoid memory leaks
        _binding = null
    }
}