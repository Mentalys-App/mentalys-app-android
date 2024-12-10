package com.mentalys.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAuthRegisterBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class AuthRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthRegisterBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this@AuthRegisterActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthRegisterBinding.inflate(layoutInflater)
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
            backButton.setOnClickListener { finish() }
            registerButton.setOnClickListener {
                val firstName = firstNameEditText.text.toString().trim()
                val lastName = lastNameEditText.text.toString().trim()
                val username = usernameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                val phoneNumber = phoneNumberEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                val confirmPassword = confirmPasswordEditText.text.toString().trim()

                // Regex pattern for phone numbers with country code
                val phoneNumberPattern = Regex("^\\+[1-9]\\d{1,14}$")

                // Validate each field
                when {
                    firstName.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "First name is required")
                        firstNameEditText.requestFocus()
                    }

                    lastName.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Last name is required")
                        lastNameEditText.requestFocus()
                    }

                    username.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Username is required")
                        usernameEditText.requestFocus()
                    }

                    email.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Email is required")
                        emailEditText.requestFocus()
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        showToast(this@AuthRegisterActivity, "Invalid email format")
                        emailEditText.requestFocus()
                    }

                    phoneNumber.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Phone number is required")
                        phoneNumberEditText.requestFocus()
                    }

                    !phoneNumberPattern.matches(phoneNumber) -> {
                        showToast(
                            this@AuthRegisterActivity,
                            "Invalid phone number format. Example: +1234567890"
                        )
                        phoneNumberEditText.requestFocus()
                    }

                    password.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Password is required")
                        passwordEditText.requestFocus()
                    }

                    password.length < 8 -> {
                        showToast(
                            this@AuthRegisterActivity,
                            "Password must be at least 8 characters"
                        )
                        passwordEditText.requestFocus()
                    }

                    confirmPassword.isEmpty() -> {
                        showToast(this@AuthRegisterActivity, "Confirm Password is required")
                        confirmPasswordEditText.requestFocus()
                    }

                    password != confirmPassword -> {
                        showToast(this@AuthRegisterActivity, "Passwords do not match")
                        confirmPasswordEditText.requestFocus()
                    }

                    else -> {
                        // All validations passed; proceed to register
                        viewModel.registerUser(
                            firstName = firstName,
                            lastName = lastName,
                            username = username,
                            email = email,
                            phoneNumber = phoneNumber,
                            password = password,
                            confirmPassword = confirmPassword
                        )
                    }
                }
            }

            loginTextView.setOnClickListener {
                val intent = Intent(this@AuthRegisterActivity, AuthLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupObserver() {
        viewModel.registerResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.registerButton.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.registerButton.isEnabled = true
                    resource.data.message?.let { showToast(this@AuthRegisterActivity, it) }
                    val intent = Intent(this, AuthEmailSentActivity::class.java)
                    intent.putExtra(AuthEmailSentActivity.EXTRA_MESSAGE, getString(R.string.email_verification_message))
                    startActivity(intent)
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.registerButton.isEnabled = true
                    showToast(this@AuthRegisterActivity, "Error: ${resource.error}")
                }
            }
        }
    }

}