package qi.mybudget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import qi.mybudget.databinding.ActivitySignUpscreenBinding // Import the binding class

class SignUPScreen : AppCompatActivity() {

    // Declare the binding variable for your layout
    private lateinit var binding: ActivitySignUpscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-new"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val users: List<User> = userDao.getAll()

        // Set up View Binding
        binding = ActivitySignUpscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener on your "Create Account" button
        binding.btnCreateAccount.setOnClickListener {
            // Here you would typically save the user's data first

            //Q Check if name available and check next id
            //Q My id check is probably redundant because auto generated but to be safe
            var nameAvailable = true
            var id = 0
            for(user in users) {
                if (binding.etUsername.text.toString() == user.username)
                {
                    nameAvailable = false
                }

                id++
            }

            //Q Create user
            if (nameAvailable)
            {
                userDao.register(User(id,
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    binding.etUsername.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()))
            }

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