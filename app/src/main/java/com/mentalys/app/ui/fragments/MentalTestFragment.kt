package com.mentalys.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.mentalys.app.databinding.FragmentMentalTestBinding
import com.mentalys.app.ui.activities.HandwritingTestActivity
import com.mentalys.app.ui.activities.QuizTestActivity
import com.mentalys.app.ui.activities.VoiceTestActivity


class MentalTestFragment : Fragment() {
    private var _binding: FragmentMentalTestBinding? = null
    private val binding get() = _binding!!
    private var selectedMenu: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMentalTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuVoice = binding.menuVoiceTest
        menuVoice.setOnClickListener {
            setSelectedMenu(menuVoice)
            startActivity(Intent(requireContext(), VoiceTestActivity::class.java))
        }

        // test handwriting
        val menuHandWriting = binding.menuHandwritingTest
        menuHandWriting.setOnClickListener {
            setSelectedMenu(menuHandWriting)
            startActivity(Intent(requireContext(), HandwritingTestActivity::class.java))
        }

        // test quiz
        val menuQuiz = binding.menuQuizTest
        menuQuiz.setOnClickListener {
            setSelectedMenu(menuQuiz)
            startActivity(Intent(requireContext(), QuizTestActivity::class.java))
        }
    }

    private fun setSelectedMenu(menu: LinearLayout) {
        binding.menuVoiceTest.isSelected = false
        binding.menuHandwritingTest.isSelected = false
        binding.menuQuizTest.isSelected = false

        menu.isSelected = true
        selectedMenu = menu
    }

    override fun onResume() {
        super.onResume()
        binding.menuVoiceTest.isSelected = false
        binding.menuHandwritingTest.isSelected = false
        binding.menuQuizTest.isSelected = false
        selectedMenu = null
    }
}