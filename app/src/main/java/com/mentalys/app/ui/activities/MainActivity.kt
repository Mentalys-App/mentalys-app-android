package com.mentalys.app.ui.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.CONSUMED
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMainBinding
import com.mentalys.app.ui.onboarding.OnboardingActivity
import com.mentalys.app.ui.viewmodels.ArticleViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ArticleViewModel by viewModels {
        ViewModelFactory.getInstance(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Check if onboarding has been shown
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        } else {
            init()
        }

    }

    private fun init() {
        // language setup
        lifecycleScope.launch {
            val savedLanguage =
                SettingsPreferences.getInstance(dataStore).getLanguageSetting().firstOrNull()
                    ?: "English"
            val locale = when (savedLanguage) {
                "Bahasa Indonesia" -> Locale("in")
                else -> Locale("en")
            }
            Locale.setDefault(locale)
            val config = Configuration(resources.configuration)
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        // theme setup
        viewModel.getThemeSetting().observe(this) { isDark ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // insets
            return@setOnApplyWindowInsetsListener CONSUMED
        }

        // setup bottom nav with navigation
        val bottomNav: BottomNavigationView = binding.bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
    }


}