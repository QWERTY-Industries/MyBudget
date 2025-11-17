package qi.mybudget

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import qi.mybudget.databinding.ActivityLoginScreenBinding // Import the binding class

class LoginScreen : AppCompatActivity() {

    // Declare the binding variable
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null)
        {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //Q Create Database Instance
//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "database-new"
//        ).allowMainThreadQueries().build()

        firebaseAuth = FirebaseAuth.getInstance()

        //var temp = getDatabasePath("database-name.db").absolutePath

//        val userDao = db.userDao()
//        val users: List<User> = userDao.getAll()

        // Set up View Binding
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, HomeScreen::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email or password is empty!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignUp.setOnClickListener {
            // Create an Intent to go from this screen to the SignupScreen
            val intent = Intent(this, SignUPScreen::class.java)

            // Start the SignupScreen activity
            startActivity(intent)

            // DO NOT call finish() here.
        }

        //Q Room db stuff vvv

//        // Find your login button using the binding object and set a click listener
//        binding.btnLogin.setOnClickListener {
//            //Q Check if name exists
//            var nameExists = false
//            var id = 0
//            for(user in users) {
//                if (binding.etUsername.text.toString() == user.username)
//                {
//                    nameExists = true
//                    //id--
//                    break
//                }
//
//                id++
//            }
//
//            //Q Log in
//            if (nameExists)
//            {
//                // Create an Intent to specify which activity to start
//                val intent = Intent(this, HomeScreen::class.java)
//                //intent.putExtra("userId", id)
//
//                // Start the HomeScreen activity
//                startActivity(intent)
//                finish()
//            }
//            else{
//                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
//            }
//
//            // Optional: Call finish() to prevent the user from returning
//            // to the login screen by pressing the back button.
//
//        }
    }
}