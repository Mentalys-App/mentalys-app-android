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
                val email = loginEdittextEmail.text.toString().trim()
                val password = loginEdittextPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    showToast(this@AuthLoginActivity, "Please fill all fields")
                    return@setOnClickListener
                }

                viewModel.loginUser(email, password)
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
                    resource.data.message?.let { showToast(this@AuthLoginActivity, it) }
                    resource.data.data?.let {
                        viewModel.saveUserLoginSession(
                            uid = it.uid,
                            token = it.idToken,
                            email = it.email
                        ) {
                            val intent = Intent(this@AuthLoginActivity, MainActivity::class.java)
                            showToast(this@AuthLoginActivity, it.email + it.idToken)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
//                        finish()
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