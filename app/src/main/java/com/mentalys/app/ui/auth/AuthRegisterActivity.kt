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
import com.mentalys.app.utils.showSnackbar
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
                        showSnackbar(binding.root, getString(R.string.error_first_name_required))
                        firstNameEditText.requestFocus()
                    }

                    firstName.length < 3 || firstName.length > 50 -> {
                        showSnackbar(binding.root, "First name must be between 3 and 50 characters")
                        firstNameEditText.requestFocus()
                    }

                    lastName.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_last_name_required))
                        lastNameEditText.requestFocus()
                    }

                    lastName.length < 3 || lastName.length > 50 -> {
                        showSnackbar(binding.root, "Last name must be between 3 and 50 characters")
                        lastNameEditText.requestFocus()
                    }

                    username.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_username_required))
                        usernameEditText.requestFocus()
                    }

                    username.length < 5 || username.length > 30 -> {
                        showSnackbar(binding.root, "Username must be between 5 and 30 characters")
                        usernameEditText.requestFocus()
                    }

                    username.contains(" ") -> {
                        showSnackbar(binding.root, "Username cannot contain spaces")
                        usernameEditText.requestFocus()
                    }

                    email.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_email_required))
                        emailEditText.requestFocus()
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        showSnackbar(binding.root, getString(R.string.error_invalid_email))
                        emailEditText.requestFocus()
                    }

                    phoneNumber.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_phone_number_required))
                        phoneNumberEditText.requestFocus()
                    }

                    !phoneNumberPattern.matches(phoneNumber) -> {
                        showSnackbar(binding.root, getString(R.string.error_invalid_phone_number))
                        phoneNumberEditText.requestFocus()
                    }

                    password.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_password_required))
                        passwordEditText.requestFocus()
                    }

                    password.length < 8 -> {
                        showSnackbar(binding.root, getString(R.string.error_password_length))
                        passwordEditText.requestFocus()
                    }

                    confirmPassword.isEmpty() -> {
                        showSnackbar(binding.root, getString(R.string.error_confirm_password_required))
                        confirmPasswordEditText.requestFocus()
                    }

                    password != confirmPassword -> {
                        showSnackbar(binding.root, getString(R.string.error_passwords_do_not_match))
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