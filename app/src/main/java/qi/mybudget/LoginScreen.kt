package qi.mybudget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import qi.mybudget.databinding.ActivityLoginScreenBinding // Import the binding class

class LoginScreen : AppCompatActivity() {

    // Declare the binding variable
    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up View Binding
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find your login button using the binding object and set a click listener
        binding.btnLogin.setOnClickListener {
            // Create an Intent to specify which activity to start
            val intent = Intent(this, HomeScreen::class.java)

            // Start the HomeScreen activity
            startActivity(intent)

            // Optional: Call finish() to prevent the user from returning
            // to the login screen by pressing the back button.
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            // Create an Intent to go from this screen to the SignupScreen
            val intent = Intent(this, SignUPScreen::class.java)

            // Start the SignupScreen activity
            startActivity(intent)

            // DO NOT call finish() here.
        }
    }
}