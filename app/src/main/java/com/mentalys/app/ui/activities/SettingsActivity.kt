package com.mentalys.app.ui.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivitySettingsBinding
import com.mentalys.app.ui.viewmodels.MainViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsPreferences: SettingsPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this@SettingsActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize settingsPreferences here
        settingsPreferences = SettingsPreferences.getInstance(dataStore)

        // Theme
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isDark ->
            viewModel.saveThemeSetting(isDark)
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Notifications
        binding.notificationSwitch.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.saveNotificationSetting(isEnabled)
        }

        // Language
        binding.languageButton.setOnClickListener {
            showLanguageSelectionDialog()
        }

        // Change switch check depend on settings saved
        lifecycleScope.launch {
            settingsPreferences.getThemeSetting().collect { isDark ->
                binding.darkModeSwitch.isChecked = isDark
            }
        }

        lifecycleScope.launch {
            settingsPreferences.getNotificationSetting().collect { isEnabled ->
                binding.notificationSwitch.isChecked = isEnabled
            }
        }

        // add comment idk
        viewModel.getThemeSetting().observe(this) {
            binding.darkModeSwitch.isChecked = it
        }

        viewModel.getNotificationSetting().observe(this) {
            binding.notificationSwitch.isChecked = it
        }

    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Bahasa Indonesia")
        lifecycleScope.launch {
            val savedLanguage = settingsPreferences.getLanguageSetting().firstOrNull() ?: "English"
            var selectedLanguage = languages.indexOf(savedLanguage).takeIf { it >= 0 } ?: 0

            AlertDialog.Builder(this@SettingsActivity)
                .setTitle("Select Language")
                .setSingleChoiceItems(languages, selectedLanguage) { _, which ->
                    selectedLanguage = which
                }
                .setPositiveButton("OK") { _, _ ->
                    val chosenLanguage = languages[selectedLanguage]
                    saveLanguageSettings(chosenLanguage)
                    applyLanguage(chosenLanguage)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun saveLanguageSettings(language: String) {
        lifecycleScope.launch {
            settingsPreferences.saveLanguageSetting(language)
        }
    }

    private fun applyLanguage(language: String) {
        val locale = when (language) {
            "Bahasa Indonesia" -> Locale("in")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        restartApp()
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}