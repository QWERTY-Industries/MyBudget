package qi.mybudget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import qi.mybudget.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(private var transactions: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    // Currency formatter for South African Rand
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    // This class holds the views for a single item
    inner class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.binding.apply {
            tvTransactionCategory.text = transaction.category
            tvTransactionDescription.text = transaction.description.takeIf { !it.isNullOrEmpty() } ?: transaction.transactionType

            val amount = transaction.amount ?: 0.0
            val formattedAmount = currencyFormatter.format(amount)

            // Set text color based on transaction type
            if (transaction.transactionType == "Expense") {
                tvTransactionAmount.text = "-$formattedAmount"
                tvTransactionAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            } else {
                tvTransactionAmount.text = "+$formattedAmount"
                tvTransactionAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            }
        }
    }

    override fun getItemCount(): Int = transactions.size

    // A function to update the data in the adapter
    fun updateData(newTransactions: List<Transaction>) {
        this.transactions = newTransactions
        notifyDataSetChanged() // Reload the entire list
    }
}