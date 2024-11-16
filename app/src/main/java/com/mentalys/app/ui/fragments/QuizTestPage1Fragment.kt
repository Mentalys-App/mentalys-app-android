package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentQuizTestPage1Binding
import com.mentalys.app.ui.activities.QuizTestActivity

class QuizTestPage1Fragment : Fragment() {

    private var _binding: FragmentQuizTestPage1Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage1Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizPage1BtnNext.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage2Fragment())
        }
    }
}