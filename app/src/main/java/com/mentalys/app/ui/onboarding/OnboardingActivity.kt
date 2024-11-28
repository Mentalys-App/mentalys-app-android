package com.mentalys.app.ui.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {

            liquidPager.adapter = OnboardingAdapter(supportFragmentManager)
            liquidPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> liquidPager.setButtonDrawable(R.drawable.ic_button_onboarding1)
                        1 -> liquidPager.setButtonDrawable(R.drawable.ic_button_onboarding2)
                        2 -> liquidPager.setButtonDrawable(R.drawable.ic_button_onboarding1)
                        3 -> liquidPager.setButtonDrawable(R.drawable.ic_button_onboarding2)
                        else -> ContextCompat.getColor(this@OnboardingActivity, R.color.primary)
                    }

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

            })

        }

    }
}