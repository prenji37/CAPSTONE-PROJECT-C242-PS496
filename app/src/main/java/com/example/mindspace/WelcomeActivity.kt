package com.example.mindspace

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mindspace.databinding.ActivityWelcomeBinding
import com.example.mindspace.ui.login.LoginActivity
import com.example.mindspace.ui.signup.RegistrationActivity
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateBackgroundRGB(binding.root)

//        checkLoginState()

        binding.welcomeText.translationY = 500f
        binding.registerButton.translationY = 500f
        binding.loginButton.translationY = 500f
        binding.logoImage.translationY = 500f

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun checkLoginState() {
//        lifecycleScope.launch {
//            val isLoggedIn = dataStoreManager.tokenFlow.first()?.isNotEmpty() == true
//            if (isLoggedIn) {
//                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//    }

    private fun animateBackgroundRGB(layout: LinearLayout) {
        // Define the colors to cycle through
        val colors = intArrayOf(
            Color.parseColor("#80FF7F7F"), // Soft red
            Color.parseColor("#807FFF7F"), // Soft green
            Color.parseColor("#807F7FFF"), // Soft blue
            Color.parseColor("#80FF7FBF"), // Soft magenta
            Color.parseColor("#807FFFFF"), // Soft cyan
            Color.parseColor("#80FFFF7F")  // Soft yellow
        )

        animateFlyIn()

        // Convert IntArray to Array<Int> using toTypedArray()
        val colorArray = colors.toTypedArray()

        // Create an animator with these colors
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), *colorArray) // Spread operator works now
        colorAnimator.duration = 30000 // 5 seconds for full animation
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        // Update background color during animation
        colorAnimator.addUpdateListener { animator ->
            layout.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimator.start()
    }

    private fun animateFlyIn() {
        // Animate Image (starts first)
        val imageAnim = ObjectAnimator.ofFloat(binding.logoImage, "translationY", 500f, 0f)
        imageAnim.duration = 1000
        imageAnim.startDelay = 0 // Starts immediately
        imageAnim.interpolator = AccelerateDecelerateInterpolator()

        // Animate Welcome Text
        val welcomeTextAnim = ObjectAnimator.ofFloat(binding.welcomeText, "translationY", 500f, 0f)
        welcomeTextAnim.duration = 1000
        welcomeTextAnim.startDelay = 200 // Starts after image (200ms delay)
        welcomeTextAnim.interpolator = AccelerateDecelerateInterpolator()

        // Animate Register Button
        val registerButtonAnim = ObjectAnimator.ofFloat(binding.registerButton, "translationY", 500f, 0f)
        registerButtonAnim.duration = 1000
        registerButtonAnim.startDelay = 400 // Starts after Welcome text (400ms delay)
        registerButtonAnim.interpolator = AccelerateDecelerateInterpolator()

        // Animate Login Button
        val loginButtonAnim = ObjectAnimator.ofFloat(binding.loginButton, "translationY", 500f, 0f)
        loginButtonAnim.duration = 1000
        loginButtonAnim.startDelay = 600 // Starts after Register button (600ms delay)
        loginButtonAnim.interpolator = AccelerateDecelerateInterpolator()

        // Start all animations at once, with staggered delays
        imageAnim.start()
        welcomeTextAnim.start()
        registerButtonAnim.start()
        loginButtonAnim.start()
    }

}
