package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mentalys.app.databinding.FragmentQuizTestPage3Binding
import com.mentalys.app.ui.activities.QuizTestActivity

class QuizTestPage3Fragment : Fragment() {

    private var _binding: FragmentQuizTestPage3Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage3Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizPage3BtnBack.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage2Fragment())
        }
    }
}