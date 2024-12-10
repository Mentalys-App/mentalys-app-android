package com.mentalys.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentHomeBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.ui.clinic.ClinicActivity
import com.mentalys.app.ui.specialist.SpecialistHomeAdapter
import com.mentalys.app.ui.dailytips.DailyTips
import com.mentalys.app.ui.dailytips.DailyTipsAdapter
import com.mentalys.app.ui.mental.test.handwriting.MentalTestHandwritingActivity
import com.mentalys.app.ui.mental.test.quiz.MentalTestQuizTestActivity
import com.mentalys.app.ui.mental.test.voice.MentalTestVoiceActivity
import com.mentalys.app.ui.specialist.SpecialistActivity
import com.mentalys.app.ui.specialist.SpecialistViewModel
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val specialistViewModel: SpecialistViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var specialistAdapter: SpecialistHomeAdapter
    private lateinit var fullName: String
    private lateinit var firstName: String

    private val carouselItems = listOf(
        DailyTips(
            title = "Mindfulness Tip",
            description = "Take 5 minutes today to focus on your breathing. Inhale deeply for 4 seconds, hold for 4 seconds, and exhale for 4 seconds.",
            imageResource = R.drawable.icon_calm
        ),
        DailyTips(
            title = "Positive Thinking",
            description = "Start your day by saying one positive thing about yourself. It can be a small achievement or a quality you admire.",
            imageResource = R.drawable.icon_happy
        ),
        DailyTips(
            title = "Stress Relief",
            description = "When feeling overwhelmed, take a break and step outside for fresh air. A few minutes in nature can help reset your mind.",
            imageResource = R.drawable.icon_nature
        ),
        DailyTips(
            title = "Gratitude Exercise",
            description = "Write down 3 things you're grateful for today. It can help shift your mindset to a more positive one.",
            imageResource = R.drawable.icon_book
        ),
        DailyTips(
            title = "Physical Activity Tip",
            description = "Try a quick 10-minute stretch session. It helps release tension and boosts mood!",
            imageResource = R.drawable.icon_exercise
        ),
        DailyTips(
            title = "Sleep Hygiene",
            description = "To improve sleep, avoid screens for at least 30 minutes before bed. Let your mind wind down.",
            imageResource = R.drawable.icon_bed
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialist()
        setupDailyTipsRecyclerView()

        // Set greeting and name
        viewLifecycleOwner.lifecycleScope.launch {
            fullName =
                SettingsPreferences.getInstance(requireContext().dataStore).getFullNameSetting()
                    .first()
            firstName = fullName.split(" ").firstOrNull() ?: "Guest"
            binding.greetingTextView.text = getGreetingMessage()
            binding.timeIcon.setImageResource(getIconResource())
            binding.nameTextView.text = "Hello, $firstName"
        }

        // Set banner images
        Glide.with(requireActivity()).load(R.drawable.image_banner_music).into(binding.imageView)
        Glide.with(requireActivity()).load(R.drawable.image_banner_meditation).into(binding.mainBannerImageView)

        // Setup click listeners and other initializations
        setupClickListeners()
        setupSpecialist()

    }

    private fun setupClickListeners() {
        binding.apply {
            lifecycleScope.launch {
                val isLogin = SettingsPreferences.getInstance(requireContext().dataStore).getIsLoginSetting().first()
                if (isLogin) {
                    questionnaireLayout.setOnClickListener {
                        val intent = Intent(requireContext(), MentalTestQuizTestActivity::class.java)
                        startActivity(intent)
                    }
                    voiceLayout.setOnClickListener {
                        val intent = Intent(requireContext(), MentalTestVoiceActivity::class.java)
                        startActivity(intent)
                    }
                    handwritingLayout.setOnClickListener {
                        val intent = Intent(requireContext(), MentalTestHandwritingActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    questionnaireLayout.setOnClickListener {
                        showToast(requireActivity(), "Please login to use this feature")
                    }
                    voiceLayout.setOnClickListener {
                        showToast(requireActivity(), "Please login to use this feature")
                    }
                    handwritingLayout.setOnClickListener {
                        showToast(requireActivity(), "Please login to use this feature")
                    }
                }
            }

            clinicLayout.setOnClickListener {
                startActivity(Intent(requireContext(), ClinicActivity::class.java))
            }
            specialistViewAllLabel.setOnClickListener {
                startActivity(Intent(requireContext(), SpecialistActivity::class.java))
            }
        }
    }

    private fun setupDailyTipsRecyclerView() {
        // Set up RecyclerView with horizontal layout manager
        binding.dailyTipsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dailyTipsRecyclerView.adapter = DailyTipsAdapter(carouselItems)
        // SnapHelper to enable snapping
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.dailyTipsRecyclerView)
        // Add indicator dots
        addIndicatorDots(carouselItems.size)
        // Ensure the initial indicator is set after layout is ready
        binding.dailyTipsRecyclerView.post {
            updateIndicator(binding.dailyTipsRecyclerView)
        }
        // Listener for scroll state changes
        binding.dailyTipsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updateIndicator(recyclerView)
            }
        })
    }

    private fun addIndicatorDots(count: Int) {
        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            dot.setImageResource(R.drawable.indicator_dot) // Create a drawable for your indicator dot
            val params = LinearLayout.LayoutParams(16, 16)
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            binding.dailyTipsIndicator.addView(dot)
        }
    }

    private fun updateIndicator(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val snapHelper = LinearSnapHelper() // Ensure you are using the same SnapHelper
        val snappedView = snapHelper.findSnapView(layoutManager)
        val position = snappedView?.let { layoutManager.getPosition(it) } ?: RecyclerView.NO_POSITION

        // Update the indicator's selected dot
        for (i in 0 until binding.dailyTipsIndicator.childCount) {
            val dot = binding.dailyTipsIndicator.getChildAt(i) as ImageView
            dot.setImageResource(if (i == position) R.drawable.indicator_dot_selected else R.drawable.indicator_dot)
            Log.d("indi", i.toString())
        }
    }

    private fun setupSpecialist() {

        specialistAdapter = SpecialistHomeAdapter()

        // Trigger fetching of specialists
        specialistViewModel.getSpecialists()

        // Observe specialists LiveData
        specialistViewModel.specialists.observe(requireActivity()) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    specialistAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    specialistAdapter.setLoadingState(false)
                    specialistAdapter.submitList(resource.data)
                    Log.d("SpecialistActivity", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(requireContext(), resource.error)
                    Log.d("SpecialistActivity", "resource.data.toString()")
                }
            }
        }

        binding.specialistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = specialistAdapter
        }

    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun getIconResource(): Int {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..20 -> R.drawable.ic_outline_sun // Replace with your sun icon resource
            else -> R.drawable.ic_outline_dark // Replace with your moon icon resource
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}