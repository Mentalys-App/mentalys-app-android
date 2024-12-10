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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMainBinding
import com.mentalys.app.ui.article.ArticleFragment
import com.mentalys.app.ui.home.HomeFragment
import com.mentalys.app.ui.mental.MentalTestFragment
import com.mentalys.app.ui.mental.history.MentalHistoryFragment
import com.mentalys.app.ui.onboarding.OnboardingActivity
import com.mentalys.app.ui.profile.ProfileFragment
import com.mentalys.app.ui.profile.ProfileLoggedOutFragment
import com.mentalys.app.ui.viewmodels.MainViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this@MainActivity)
    }

    private var isLoggedIn: Boolean? = false

    // Fragment instances
    private val homeFragment = HomeFragment()
    private val articleFragment = ArticleFragment()
    private val mentalHistoryFragment = MentalHistoryFragment()
    private val profileFragment = ProfileFragment()
    private val profileLoggedOutFragment = ProfileLoggedOutFragment()

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
            lifecycleScope.launch {
                isLoggedIn = SettingsPreferences.getInstance(dataStore).getIsLoginSetting().first()
                init()

                // Check if a specific fragment should be shown
                intent?.getIntExtra("FRAGMENT_TO_SHOW", -1)?.let { fragmentId ->
                    if (fragmentId == 4) {
                        binding.bottomNav.show(4, true)
                        showFragment(profileLoggedOutFragment)
                    }
                }
            }
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

        // Initialize all fragments and add them to the FragmentManager
        initializeFragments()

        // Set up MeowBottomNavigation
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottom_nav)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_round_home))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_round_book))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_round_reports))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_round_person))
        bottomNavigation.setOnClickMenuListener { model ->
            when (model.id) {
                1 -> showFragment(homeFragment)
                2 -> showFragment(articleFragment)
                3 -> showFragment(mentalHistoryFragment)
                4 -> {
                    // Check login state when the 5th menu item (Profile) is clicked
                    if (isLoggedIn == true) {
                        showFragment(profileFragment) // Show ProfileFragment if logged in
                    } else {
                        showFragment(profileLoggedOutFragment) // Show ProfileLoggedOutFragment if not logged in
                    }
                }
            }
        }
        bottomNavigation.setOnShowListener { }
        bottomNavigation.setOnReselectListener { }
        bottomNavigation.show(1, true)
    }

    private fun initializeFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.nav_host_fragment, homeFragment, "Home")
        transaction.add(R.id.nav_host_fragment, articleFragment, "Education").hide(articleFragment)
        transaction.add(R.id.nav_host_fragment, mentalHistoryFragment, "Reports")
            .hide(mentalHistoryFragment)
        // Check login state when the 5th menu item (Profile) is clicked
        if (isLoggedIn == true) {
            showToast(this@MainActivity, isLoggedIn.toString())
            transaction.add(R.id.nav_host_fragment, profileFragment, "Profile")
                .hide(profileFragment)
        } else {
            showToast(this@MainActivity, isLoggedIn.toString())
            transaction.add(
                R.id.nav_host_fragment,
                profileLoggedOutFragment,
                "Profile"
            ).hide(profileLoggedOutFragment)
        }
        transaction.commit()
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach { transaction.hide(it) }
        transaction.show(fragment).commit()
    }

}