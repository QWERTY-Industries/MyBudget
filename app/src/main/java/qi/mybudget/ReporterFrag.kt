package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import qi.mybudget.databinding.FragmentReporterBinding
import java.text.NumberFormat
import java.util.Locale

class ReporterFrag : Fragment() {

    private var _binding: FragmentReporterBinding? = null
    private val binding get() = _binding!!

    // --- CHANGE 1: Create a mutable list for goals and the adapter ---
    private val goalList = mutableListOf<Goal>()
    private lateinit var goalsAdapter: GoalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReporterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // No need to bind again, onCreateView already does it if you return binding.root

        // --- CHANGE 2: Initialize the adapter with the (initially empty) list ---
        goalsAdapter = GoalsAdapter(goalList)

        // Set up the RecyclerView
        binding.goalsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = goalsAdapter
        }

        // --- CHANGE 3: Add some initial sample data (optional, but good for testing) ---
        addSampleGoals()

        // Set up all the button click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // ... (Your other button listeners like btnAddition, btnSubtraction remain the same)
        binding.btnAddition.setOnClickListener { performCalculation("+", false) }
        binding.btnSubtraction.setOnClickListener { performCalculation("-", false) }
        binding.btnMultiplication.setOnClickListener { performCalculation("*", false) }
        binding.btnDivision.setOnClickListener { performCalculation("/", false) }
        binding.equalTo.setOnClickListener { performCalculation("/", false) }


        // --- CHANGE 4: The main "Calculate" button will now also save the goal ---
        binding.button3.setOnClickListener {
            // The 'true' flag tells the function to save this as a new goal
            performCalculation("/", true)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // --- CHANGE 5: Modify performCalculation to accept a 'saveGoal' flag ---
    private fun performCalculation(operator: String, saveGoal: Boolean) {
        val goalAmountStr = binding.etUserfirstValue.text.toString()
        val timeInWeeksStr = binding.etUserSecondValue.text.toString()
        // CHANGE 2: Get the text from the new title EditText
        val goalTitle = binding.etGoalTitle.text.toString()

        if (goalAmountStr.isEmpty() || timeInWeeksStr.isEmpty()) {
            Toast.makeText(context, "Please enter Goal Amount and Time", Toast.LENGTH_SHORT).show()
            return
        }

        // CHANGE 3: Also check if the title is empty when saving a goal
        if (saveGoal && goalTitle.isBlank()) {
            Toast.makeText(context, "Please enter a title for your goal", Toast.LENGTH_SHORT).show()
            // Optional: You can request focus to guide the user
            binding.etGoalTitle.requestFocus()
            return
        }

        val goalAmount = goalAmountStr.toDoubleOrNull()
        val timeInWeeks = timeInWeeksStr.toDoubleOrNull()

        if (goalAmount == null || timeInWeeks == null) {
            Toast.makeText(context, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            return
        }

        if (operator == "/" && timeInWeeks == 0.0) {
            Toast.makeText(context, "Time cannot be zero", Toast.LENGTH_SHORT).show()
            return
        }

        val result = goalAmount / timeInWeeks
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val resultText = "Save ${currencyFormat.format(result)} per week"

        binding.editTextText.setText(resultText)

        // CHANGE 4: If saveGoal is true, create and add the new goal with the title
        if (saveGoal) {
            // Use the user's title if provided, otherwise create a default one.
            val description = "$goalTitle: Save ${currencyFormat.format(goalAmount)} in ${timeInWeeks.toInt()} weeks"

            val newGoal = Goal(
                description = description, // Use the new combined description
                targetAmount = goalAmount,
                currentAmount = 0.0, // A new goal starts with 0 progress
                timeInWeeks = timeInWeeks.toInt()
            )

            // Add to list and notify the adapter
            goalList.add(0, newGoal) // Add to the top of the list
            goalsAdapter.notifyItemInserted(0)
            binding.goalsRecyclerView.scrollToPosition(0) // Scroll to the new item

            // Clear the input fields for the next goal
            binding.etUserfirstValue.text.clear()
            binding.etUserSecondValue.text.clear()
            binding.etGoalTitle.text.clear()
            binding.editTextText.text.clear()

            Toast.makeText(context, "Goal Set!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addSampleGoals() {
        // This is just for demonstration. You can remove this later.
        goalList.add(Goal("Save for vacation", 5000.0, 1500.0, 12))
        goalList.add(Goal("New Laptop", 12000.0, 8000.0, 8))
        goalsAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}