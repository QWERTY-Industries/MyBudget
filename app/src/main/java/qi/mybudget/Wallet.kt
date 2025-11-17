package qi.mybudget

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Represents the user's wallet balances.
 * Using Double? allows Firebase to handle missing fields gracefully.
 */
@IgnoreExtraProperties
data class Wallet(
    val cashBalance: Double? = 0.0,
    val cardBalance: Double? = 0.0,
    val savingsBalance: Double? = 0.0
) {
    // A no-argument constructor is required by Firebase for deserialization
    constructor() : this(0.0, 0.0, 0.0)
}