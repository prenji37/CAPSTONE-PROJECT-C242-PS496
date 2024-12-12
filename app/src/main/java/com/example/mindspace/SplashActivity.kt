package com.example.mindspace

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mindspace.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate the background color smoothly
        animateBackgroundRGB(binding.root)

        animateIconPopOut(binding.splashImage)

        // Delay to start WelcomeActivity after splash screen
        binding.root.postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 5 seconds splash screen time
    }

    private fun animateBackgroundRGB(layout: RelativeLayout) {
        // Define the colors to cycle through
        val colors = intArrayOf(
            Color.parseColor("#FF007F"), // Hot pink
            Color.parseColor("#FF1493"), // Deep pink
            Color.parseColor("#FF1E73"), // Slightly darker pink
            Color.parseColor("#FF3385"), // Hotter magenta-pink
            Color.parseColor("#D1006A"), // Darker magenta-pink
            Color.parseColor("#B20063"), // Dark magenta-pink
            Color.parseColor("#9B0062"), // Deep magenta-pink
            Color.parseColor("#8B008B"), // Dark magenta
            Color.parseColor("#7A006D"), // Even darker magenta
            Color.parseColor("#6A004F")  // Deepest dark magenta
        )

        // Convert IntArray to Array<Int> using toTypedArray()
        val colorArray = colors.toTypedArray()

        // Create an animator with these colors
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), *colorArray) // Spread operator works now
        colorAnimator.duration = 2000
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        // Update background color during animation
        colorAnimator.addUpdateListener { animator ->
            layout.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimator.start()
    }

    private fun animateIconPopOut(icon: ImageView) {
        icon.scaleX = 0f
        icon.scaleY = 0f
        icon.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}
