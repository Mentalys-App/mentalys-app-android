package com.mentalys.app.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAuthResetPasswordBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class AuthResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthResetPasswordBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this@AuthResetPasswordActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthResetPasswordBinding.inflate(layoutInflater)
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
            resetPasswordButton.setOnClickListener {
                val email = resetEdittextEmail.text.toString().trim()
                if (email.isEmpty()) {
                    showToast(this@AuthResetPasswordActivity, "Please fill all fields")
                    return@setOnClickListener
                }
                viewModel.resetPassword(email)
            }
        }
    }

    private fun setupObserver() {
        viewModel.resetPasswordResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.resetPasswordButton.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.resetPasswordButton.isEnabled = true
                    resource.data.message?.let { showToast(this@AuthResetPasswordActivity, it) }
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.resetPasswordButton.isEnabled = true
                    showToast(this@AuthResetPasswordActivity, "Error: ${resource.error}")
                }
            }
        }
    }

}