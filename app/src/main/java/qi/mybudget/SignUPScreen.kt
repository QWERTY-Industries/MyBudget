package qi.mybudget

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import qi.mybudget.databinding.ActivitySignUpscreenBinding

class SignUPScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpscreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize Firebase Realtime Database reference
        // We'll store user profiles under a "users" node
        database = FirebaseDatabase.getInstance().getReference("users")

        binding.btnCreateAccount.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // --- Input Validation ---
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            return
        }

        // --- Step 1: Create the user in Firebase Authentication ---
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, now save the extra user info to the database
                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid

                    if (uid != null) {
                        saveUserInfoToDatabase(uid, firstName, lastName, username, email)
                    } else {
                        // This case is rare, but good to handle
                        Toast.makeText(baseContext, "Account creation failed, could not get user ID.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // --- Step 2: Save the additional user information to Firebase Realtime Database ---
    private fun saveUserInfoToDatabase(uid: String, firstName: String, lastName: String, username: String, email: String) {
        val user = User(uid, firstName, lastName, username, email)

        // The user's data will be saved under the path: /users/{uid}
        database.child(uid).setValue(user)
            .addOnSuccessListener {
                // Data saved successfully!
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()

                // Navigate to the main screen of the app (e.g., MainActivity or LoginScreen)
                // You might want to navigate to a login screen or directly into the app
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // Prevents user from going back to the sign-up screen
            }
            .addOnFailureListener {
                // Handle the failure case
                Toast.makeText(this, "Failed to save user details: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}

        //Q Room db stuff vvv

//        // Set a click listener on your "Create Account" button
//        binding.btnCreateAccount.setOnClickListener {
//            // Here you would typically save the user's data first
//
//            //Q Check if name available and check next id
//            //Q My id check is probably redundant because auto generated but to be safe
//            var nameAvailable = true
//            var id = 0
//            for (user in users) {
//                if (binding.etUsername.text.toString() == user.username) {
//                    nameAvailable = false
//                }
//
//                id++
//            }
//
//            //Q Create user
//            if (nameAvailable) {
//                userDao.register(
//                    User(
//                        id,
//                        binding.etFirstName.text.toString(),
//                        binding.etLastName.text.toString(),
//                        binding.etUsername.text.toString(),
//                        binding.etEmail.text.toString(),
//                        binding.etPassword.text.toString()
//                    )
//
//
//                )
//
//
//            }
//
//            // For now, we will just navigate
//
//            // Create an Intent to go from this screen to the LoginScreen
//            val intent = Intent(this, LoginScreen::class.java)
//
//            // Start the LoginScreen activity
//            startActivity(intent)
//
//            // Call finish() to close the sign-up screen so the user
//            // can't press the back button to return to it.
//            finish()
//        }
   // }//
//}