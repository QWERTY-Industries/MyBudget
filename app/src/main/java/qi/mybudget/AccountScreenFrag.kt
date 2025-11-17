package qi.mybudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AccountScreenFrag : Fragment() {

    // Firebase services
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // UI Views
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var tvEmail: TextView
    // We'll assume for now you might have an EditText for username in your layout.
    // If not, this code won't cause an error, but it won't do anything for username.

    private lateinit var btnUpdateInfo: Button
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_screen, container, false)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Find all the views from the layout
        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        tvEmail = view.findViewById(R.id.tvEmail)
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo)
        btnBack = view.findViewById(R.id.btnBack)

        // Load the user's current info
        loadUserInfo()

        // Set up the click listeners
        setupClickListeners()

        return view
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnUpdateInfo.setOnClickListener {
            updateUserInfo()
        }
    }

    private fun loadUserInfo() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Use the User class from your User.kt file
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    etFirstName.setText(it.firstName)
                    etLastName.setText(it.lastName)
                    tvEmail.text = it.email
                    // If you had an EditText for username, you would set it here:
                    // etUsername.setText(it.username)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserInfo() {
        val userId = auth.currentUser?.uid ?: return

        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        // If you had an EditText for username, you would get its value here:
        // val username = etUsername.text.toString().trim()

        // Create a map to hold the data to be updated.
        // This now matches your User.kt model.
        val userUpdates = mapOf(
            "firstName" to firstName,
            "lastName" to lastName
            // "username" to username // You would add this line if you have a username field
        )

        database.child(userId).updateChildren(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
    }
}