package com.example.mindspace.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindspace.api.ApiConfig.retrofit
import com.example.mindspace.api.ApiService
import com.example.mindspace.api.RegisterRequest
import com.example.mindspace.databinding.ActivityRegistrationBinding
import com.example.mindspace.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        showProgressBar()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = RegisterRequest(email, name, password)
                val response = apiService.registerUser(request)
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    if (response.isSuccessful) {
                        showToast("Registration successful for $name")
                        navigateToLogin()
                    } else {
                        val errorMessage = "Registration failed: ${response.code()}"
                        showToast(errorMessage)
                        Log.e("RegistrationActivity", errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMessage = "An error occurred: ${e.message}"
                    hideProgressBar()
                    showToast(errorMessage)
                    Log.e("RegistrationActivity", errorMessage, e)
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

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
