package qi.mybudget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
//import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView

// We need a data class to represent a single transaction/report item
data class TransactionReport(
    val id: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val date: String = ""
)

class ReportsAdapter(
    private var reports: List<TransactionReport>,
    private val onItemClicked: (TransactionReport) -> Unit // This is for handling clicks
) : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    // This class holds the views for a single item
    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.tvReportCategory)
        val amountTextView: TextView = itemView.findViewById(R.id.tvReportAmount)
        val dateTextView: TextView = itemView.findViewById(R.id.tvReportDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_condensed_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun getItemCount(): Int = reports.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]

        // Populate the views with data
        holder.categoryTextView.text = report.category
        holder.dateTextView.text = report.date
        holder.amountTextView.text = String.format("- R%.2f", report.amount) // Format as currency

        // Set the click listener on the item view
        holder.itemView.setOnClickListener {
            onItemClicked(report)
        }
    }

    // A helper function to update the data in the adapter
    fun updateData(newReports: List<TransactionReport>) {
        this.reports = newReports
        notifyDataSetChanged() // Refresh the list
    }
}