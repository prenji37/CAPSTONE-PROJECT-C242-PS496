package com.example.mindspace

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mindspace.databinding.ActivityWelcomeBinding
import com.example.mindspace.ui.login.LoginActivity
import com.example.mindspace.ui.signup.RegistrationActivity
import com.example.mindspace.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel: AuthViewModel by viewModels()
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        animateBackgroundRGB(binding.root)

        binding.welcomeText.translationY = 500f
        binding.registerButton.translationY = 500f
        binding.loginButton.translationY = 500f
        binding.googleSignInButton.translationY = 500f
        binding.logoImage.translationY = 500f

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authViewModel.firebaseAuthWithGoogle(this, account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun animateBackgroundRGB(layout: LinearLayout) {
        val colors = intArrayOf(
            Color.parseColor("#80FF7F7F"), // Soft red
            Color.parseColor("#807FFF7F"), // Soft green
            Color.parseColor("#807F7FFF"), // Soft blue
            Color.parseColor("#80FF7FBF"), // Soft magenta
            Color.parseColor("#807FFFFF"), // Soft cyan
            Color.parseColor("#80FFFF7F")  // Soft yellow
        )

        animateFlyIn()

        val colorArray = colors.toTypedArray()
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), *colorArray)
        colorAnimator.duration = 30000
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        colorAnimator.addUpdateListener { animator ->
            layout.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimator.start()
    }

    private fun animateFlyIn() {
        val imageAnim = ObjectAnimator.ofFloat(binding.logoImage, "translationY", 500f, 0f)
        imageAnim.duration = 1000
        imageAnim.startDelay = 0
        imageAnim.interpolator = AccelerateDecelerateInterpolator()

        val welcomeTextAnim = ObjectAnimator.ofFloat(binding.welcomeText, "translationY", 500f, 0f)
        welcomeTextAnim.duration = 1000
        welcomeTextAnim.startDelay = 200
        welcomeTextAnim.interpolator = AccelerateDecelerateInterpolator()

        val registerButtonAnim = ObjectAnimator.ofFloat(binding.registerButton, "translationY", 500f, 0f)
        registerButtonAnim.duration = 1000
        registerButtonAnim.startDelay = 400
        registerButtonAnim.interpolator = AccelerateDecelerateInterpolator()

        val loginButtonAnim = ObjectAnimator.ofFloat(binding.loginButton, "translationY", 500f, 0f)
        loginButtonAnim.duration = 1000
        loginButtonAnim.startDelay = 600
        loginButtonAnim.interpolator = AccelerateDecelerateInterpolator()

        val googleSignInButtonAnim = ObjectAnimator.ofFloat(binding.googleSignInButton, "translationY", 500f, 0f)
        googleSignInButtonAnim.duration = 1000
        googleSignInButtonAnim.startDelay = 800
        googleSignInButtonAnim.interpolator = AccelerateDecelerateInterpolator()

        imageAnim.start()
        welcomeTextAnim.start()
        registerButtonAnim.start()
        loginButtonAnim.start()
        googleSignInButtonAnim.start()
    }
}
