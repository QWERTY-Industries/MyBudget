package qi.mybudget

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import qi.mybudget.databinding.ActivityMainBinding // Make sure this import is correct

class MainActivity : AppCompatActivity() {

    // 1. Declare the binding variable for your layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. Inflate the layout using the binding class
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 3. Set the content view ONCE using the root of the binding
        setContentView(binding.root)

        // 4. Access views through the binding object (no more findViewById)
        val animationDrawable = binding.layout.background as AnimationDrawable

        // Set fade durations
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(3000)

        // Start the animation
        animationDrawable.start()

        // 5. Apply window insets listener to the root view from binding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Q Wait for splash screen
        val handler: Handler = Handler()
        handler.postDelayed(Runnable {
            val intent: Intent = Intent(
                this,
                LoginScreen::class.java
            )
            startActivity(intent)
        }, 3000)
    }
}