package qi.mybudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.your.package.name.R // <-- IMPORTANT: Change to your actual package name

class GoalsAdapter(private val goals: List<Goal>) : RecyclerView.Adapter<GoalsAdapter.ViewHolder>() {

    // This class holds the views for each item in the list
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.tvGoalDescription)
        val progressTextView: TextView = view.findViewById(R.id.tvProgressText)
        val progressBar: ProgressBar = view.findViewById(R.id.pbGoalProgress)
    }

    // Creates a new ViewHolder by inflating the item_goal.xml layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return ViewHolder(view)
    }

    // Binds the data from the 'goals' list to the views in the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goals[position]

        // Set the goal description
        holder.descriptionTextView.text = goal.description

        // Set the progress text
        holder.progressTextView.text = "R${goal.currentAmount.toInt()} / R${goal.targetAmount.toInt()}"

        // Calculate and set the progress for the ProgressBar
        if (goal.targetAmount > 0) {
            val progressPercentage = (goal.currentAmount / goal.targetAmount * 100).toInt()
            holder.progressBar.progress = progressPercentage
        } else {
            holder.progressBar.progress = 0
        }
    }

    // Returns the total number of items in the list
    override fun getItemCount() = goals.size
}