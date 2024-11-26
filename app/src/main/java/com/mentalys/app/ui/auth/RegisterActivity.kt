package com.mentalys.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityRegisterBinding
import com.mentalys.app.ui.viewmodels.AuthViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this@RegisterActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            registerButton.setOnClickListener {
                val email = loginEdittextEmail.text.toString().trim()
                val password = loginEdittextPassword.text.toString().trim()
                val confirmPassword = loginEdittextConfirmPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showToast(this@RegisterActivity, "Please fill all fields")
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    showToast(this@RegisterActivity, "Passwords do not match")
                    return@setOnClickListener
                }

                viewModel.registerUser(email, password, confirmPassword)
            }

            loginTextView.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
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
                    showToast(this@RegisterActivity, resource.data.message)
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.registerButton.isEnabled = true
                    showToast(this@RegisterActivity, "Error: ${resource.error}")
                }
            }
        }
    }

}