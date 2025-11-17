package qi.mybudget

import com.google.firebase.database.IgnoreExtraProperties


/**
 * Represents a single category, either default or user-created.
 */
@IgnoreExtraProperties
data class Category(
    val categoryId: String? = null, // Unique ID from Firebase
    val name: String? = null,
    val userId: String? = null // To know who created it
) {
    // No-argument constructor for Firebase
    constructor() : this(null, null, null)
}