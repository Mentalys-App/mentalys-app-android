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
import com.mentalys.app.databinding.ActivityLoginBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this@LoginActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
                val email = loginEdittextEmail.text.toString().trim()
                val password = loginEdittextPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    showToast(this@LoginActivity, "Please fill all fields")
                    return@setOnClickListener
                }

                viewModel.loginUser(email, password)
            }

            registerTextview.setOnClickListener {
                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupObserver() {
        viewModel.registerResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    resource.data.message?.let { showToast(this@LoginActivity, it) }
                    startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    showToast(this@LoginActivity, "Error: ${resource.error}")
                }
            }
        }
    }

}