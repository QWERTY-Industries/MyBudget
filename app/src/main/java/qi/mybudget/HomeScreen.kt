package qi.mybudget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import qi.mybudget.databinding.ActivityHomeScreenBinding
import android.graphics.drawable.AnimationDrawable
import qi.mybudget.databinding.ActivitySignUpscreenBinding

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = this.intent.getIntExtra("UserId", 0)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animationDrawable = binding.main.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bttmNavView.setupWithNavController(navController)

        // Add functionality to the Floating Action Button
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_homeFrag_to_createExpense)
        }
    }
}