package com.example.mindspace.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindspace.MainActivity
import com.example.mindspace.api.ApiConfig.retrofit
import com.example.mindspace.api.ApiService
import com.example.mindspace.api.Datastore
import com.example.mindspace.api.LoginRequest
import com.example.mindspace.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginUser(email, password)
        }

        observeLoginState()
    }

    private fun loginUser(email: String, password: String) {
        showProgressBar()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = LoginRequest(email, password)
                val response = apiService.loginUser(request)
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    if (response.isSuccessful) {
                        // Save login state in DataStore
                        Datastore.saveUserLoggedIn(this@LoginActivity, true)
                        showToast("Login successful")
                    } else if (response.code() == 500) {
                        showToast("Server error. Proceeding to main activity...")
                    } else {
                        val errorMessage = "Login failed: ${response.code()}"
                        showToast(errorMessage)
                        Log.e("LoginActivity", errorMessage)
                    }
                    navigateToMainActivity()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    val errorMessage = "An error occurred: ${e.message}"
                    showToast(errorMessage)
                    Log.e("LoginActivity", errorMessage, e)
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun observeLoginState() {
        CoroutineScope(Dispatchers.Main).launch {
            Datastore.isUserLoggedInFlow(this@LoginActivity).collect { isLoggedIn ->
                if (isLoggedIn == true) {
                    // User is already logged in, navigate to MainActivity
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
