package qi.mybudget

import com.google.firebase.database.IgnoreExtraProperties

/**
 * This data class represents a User for the Firebase Realtime Database.
 * The @IgnoreExtraProperties annotation is crucial for Firebase.
 */
@IgnoreExtraProperties
data class User(
    val uid: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val email: String? = null
) {
    // A no-argument constructor is required by Firebase for deserializing data.
    constructor() : this(null, null, null, null, null)
}