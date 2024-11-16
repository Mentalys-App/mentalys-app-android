package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mentalys.app.databinding.FragmentQuizTestPage2Binding
import com.mentalys.app.ui.activities.QuizTestActivity


class QuizTestPage2Fragment : Fragment() {

    private var _binding: FragmentQuizTestPage2Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage2Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizPage2BtnNext.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage3Fragment())
        }
        binding.quizPage2BtnBack.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage1Fragment())
        }
    }
}