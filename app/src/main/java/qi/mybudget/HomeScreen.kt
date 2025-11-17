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

        // 1. Connect the BottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)

        // 2. Manually handle clicks for the side NavigationView (Drawer)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> navController.navigate(R.id.settingsFrag)
                R.id.nav_profile -> navController.navigate(R.id.accountScreenFrag)
                R.id.nav_help -> navController.navigate(R.id.helpFragment)
                R.id.nav_logout -> {
                    Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        // 3. Define top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFrag2, R.id.reporterFrag, R.id.analysisFrag),
            drawerLayout
        )

        // 4. Set up the Toolbar
        setSupportActionBar(binding.toolbar)

        // 5. Hide the title from the Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 6. Connect the Toolbar with the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // --- NEW: SET UP THE FLOATING ACTION BUTTON ---
        binding.fab.setOnClickListener {
            // This is the ID of your destination in nav_graph.xml
            navController.navigate(R.id.addTransactionFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
