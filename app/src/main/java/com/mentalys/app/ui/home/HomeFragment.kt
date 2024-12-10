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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val carouselItems = listOf(
            DailyTips(
                title = getString(R.string.mindfulness_tip_title),
                description = getString(R.string.mindfulness_tip_description),
                imageResource = R.drawable.icon_calm
            ),
            DailyTips(
                title = getString(R.string.positive_thinking_title),
                description = getString(R.string.positive_thinking_description),
                imageResource = R.drawable.icon_happy
            ),
            DailyTips(
                title = getString(R.string.stress_relief_title),
                description = getString(R.string.stress_relief_description),
                imageResource = R.drawable.icon_nature
            ),
            DailyTips(
                title =getString(R.string.gratitude_exercise_title),
                description = getString(R.string.gratitude_exercise_description),
                imageResource = R.drawable.icon_book
            ),
            DailyTips(
                title = getString(R.string.physical_activity_tip_title),
                description = getString(R.string.physical_activity_tip_description),
                imageResource = R.drawable.icon_exercise
            ),
            DailyTips(
                title = getString(R.string.sleep_hygiene_title),
                description = getString(R.string.sleep_hygiene_description),
                imageResource = R.drawable.icon_bed
            )
        )

        setupSpecialist()
        setupDailyTipsRecyclerView(carouselItems)

        // Set greeting and name
        viewLifecycleOwner.lifecycleScope.launch {
            fullName =
                SettingsPreferences.getInstance(requireContext().dataStore).getFullNameSetting()
                    .first()
            firstName = fullName.split(" ").firstOrNull() ?: "Guest"
            binding.greetingTextView.text = getGreetingMessage()
            binding.timeIcon.setImageResource(getIconResource())
            binding.nameTextView.text = getString(R.string.greeting_user, firstName)
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
                        showToast(requireActivity(), getString(R.string.alert_login))
                    }
                    voiceLayout.setOnClickListener {
                        showToast(requireActivity(), getString(R.string.alert_login))
                    }
                    handwritingLayout.setOnClickListener {
                        showToast(requireActivity(), getString(R.string.alert_login))
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

    private fun setupDailyTipsRecyclerView(carouselItems : List<DailyTips>) {
        // Set up RecyclerView with horizontal layout manager
        binding.dailyTipsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dailyTipsRecyclerView.adapter = DailyTipsAdapter(carouselItems)
        // SnapHelper to enable snapping
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.dailyTipsRecyclerView)
        // Add indicator dots
        addIndicatorDots(carouselItems.size)
        // todo: fix initially set the first dot as selected
        updateIndicator(binding.dailyTipsRecyclerView)
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
        val position = layoutManager.findFirstVisibleItemPosition()
        // Ensure that the first dot is selected initially
        val selectedPosition = if (position == -1) 0 else position
        // Update the indicator's selected dot
        for (i in 0 until binding.dailyTipsIndicator.childCount) {
            val dot = binding.dailyTipsIndicator.getChildAt(i) as ImageView
            dot.setImageResource(if (i == selectedPosition) R.drawable.indicator_dot_selected else R.drawable.indicator_dot)
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
            in 5..11 -> getString(R.string.good_morning)
            in 12..16 -> getString(R.string.good_afternoon)
            in 17..20 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
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