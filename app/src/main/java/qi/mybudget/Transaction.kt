package qi.mybudget

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Transaction(
    val uid: String? = null,
    val transactionId: String? = null,
    val transactionType: String? = null, // "Income" or "Expense"
    val category: String? = null,
    val description: String? = null,
    val amount: Double? = null,
    val date: Long? = null // Timestamp
) {
    // Add a no-argument constructor, which is required by Firebase
    constructor() : this(null, null, null, null, null, null, null)
}