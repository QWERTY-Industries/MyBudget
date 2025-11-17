package qi.mybudget

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import qi.mybudget.databinding.ActivityHomeScreenBinding

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 1. Connect the BottomNavigationView for automatic handling
        binding.bottomNavigationView.setupWithNavController(navController)

        // 2. Manually handle clicks for the side NavigationView (Drawer)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // First, navigate to the correct destination based on the item's ID
            when (menuItem.itemId) {
                R.id.nav_settings -> navController.navigate(R.id.settingsFrag)
                R.id.nav_profile -> navController.navigate(R.id.accountScreenFrag)
                R.id.nav_help -> navController.navigate(R.id.helpFragment)
                R.id.nav_logout -> {
                    // You can add your actual logout logic here
                    Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show()
                }
            }
            // After handling the click, close the drawer
            binding.drawerLayout.closeDrawers()
            // Return true to signify the event was handled
            true
        }

        // 3. Define top-level destinations (for the hamburger icon)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFrag2, R.id.reporterFrag, R.id.analysisFrag),
            drawerLayout
        )

        // 4. Set up the Toolbar
        setSupportActionBar(binding.toolbar)

        // 5. HIDE THE TITLE FROM THE TOOLBAR
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 6. Connect the Toolbar with the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // This method handles the 'Up' button (hamburger icon or back arrow)
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
