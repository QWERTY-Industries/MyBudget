package qi.mybudget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import qi.mybudget.databinding.ActivitySignUpscreenBinding // Import the binding class

class SignUPScreen : AppCompatActivity() {

    // Declare the binding variable for your layout
    private lateinit var binding: ActivitySignUpscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up View Binding
        binding = ActivitySignUpscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener on your "Create Account" button
        binding.btnCreateAccount.setOnClickListener {
            // Here you would typically save the user's data first
            // For now, we will just navigate

            // Create an Intent to go from this screen to the LoginScreen
            val intent = Intent(this, LoginScreen::class.java)

            // Start the LoginScreen activity
            startActivity(intent)

            // Call finish() to close the sign-up screen so the user
            // can't press the back button to return to it.
            finish()
        }
    }
}