package qi.mybudget

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Budget(
    val uid: String? = null,
    val budgetId: String? = null,
    val categoryName: String? = null, // e.g., "Groceries", "Entertainment"
    val maxLimit: Double? = null,     // The spending limit
    val minLimit: Double? = null      // Optional minimum target
) {
    // No-argument constructor for Firebase
    constructor() : this(null, null, null, null, null)
}