package com.mentalys.app.ui.auth

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAuthEmailSentBinding
import com.mentalys.app.utils.showToast

class AuthEmailSentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthEmailSentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthEmailSentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()

    }

    private fun setupListeners() {
        binding.closeButton.setOnClickListener {
            val intent = Intent(this@AuthEmailSentActivity, AuthLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
        binding.goToLoginButton.setOnClickListener {
            val intent = Intent(this@AuthEmailSentActivity, AuthLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
        binding.openEmailButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_EMAIL)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                // Try to open the default email app (inbox)
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                // If no email app is found, open the web-based Gmail inbox
                val emailUrl = "https://mail.google.com"
                try {
                    val customTabsIntent = CustomTabsIntent.Builder().build()
                    customTabsIntent.launchUrl(this, Uri.parse(emailUrl))
                } catch (ex: Exception) {
                    // If no browser is available
                    showToast(this, "No email app or browser found on this device.")
                }
            }
        }

    }

}