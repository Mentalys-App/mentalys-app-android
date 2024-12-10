package com.mentalys.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAuthLoginBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class AuthLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthLoginBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this@AuthLoginActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObserver()
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                // Validate each field
                when {
                    email.isEmpty() -> {
                        showToast(this@AuthLoginActivity, "Email is required")
                        emailEditText.requestFocus()
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        showToast(this@AuthLoginActivity, "Invalid email format")
                        passwordEditText.requestFocus()
                    }

                    password.isEmpty() -> {
                        showToast(this@AuthLoginActivity, "Password is required")
                        passwordEditText.requestFocus()
                    }

                    password.length < 8 -> {
                        showToast(
                            this@AuthLoginActivity,
                            "Password must be at least 8 characters"
                        )
                        passwordEditText.requestFocus()
                    }

                    else -> {
                        // All validations passed; proceed to register
                        viewModel.loginUser(email, password)
                    }
                }

            }

            registerTextview.setOnClickListener {
                val intent = Intent(this@AuthLoginActivity, AuthRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            forgotPasswordTextView.setOnClickListener {
                val intent = Intent(this@AuthLoginActivity, AuthResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupObserver() {
        viewModel.loginResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    resource.data.message?.let {
                        showToast(this@AuthLoginActivity, it)
                    }

                    val loginData = resource.data.data
                    val profile = resource.data.profile

                    if (loginData != null && profile?.phoneNumber != null && profile.username != null && profile.full_name != null) {
                        viewModel.saveUserLoginSession(
                            uid = loginData.uid,
                            token = resource.data.idToken,
                            email = loginData.email,
                            fullName = profile.full_name,
                            username = profile.username,
                            phoneNumber = profile.phoneNumber
                        ) {
                            val intent = Intent(this@AuthLoginActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            Log.d("AuthLoginActivity", "${loginData.email} ${resource.data.idToken}")
                            startActivity(intent)
                        }
                    }

                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    showToast(this@AuthLoginActivity, "Error: ${resource.error}")
                }
            }
        }
    }

}