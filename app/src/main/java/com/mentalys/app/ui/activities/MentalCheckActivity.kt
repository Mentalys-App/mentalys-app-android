package com.mentalys.app.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMainBinding
import com.mentalys.app.databinding.ActivityMentalCheckBinding

class MentalCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentalCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val menuVoice = binding.menuVoiceTest
        val menuHandWriting = binding.menuHandwritingTest
        val menuQuiz = binding.menuQuizTest

        menuVoice.setOnClickListener {
            setSelectedMenu(menuVoice)
        }
        menuHandWriting.setOnClickListener {
            setSelectedMenu(menuHandWriting)
        }
        menuQuiz.setOnClickListener {
            setSelectedMenu(menuQuiz)
        }


    }
    private fun setSelectedMenu(selectedMenu: LinearLayout) {
        binding.menuVoiceTest.isSelected = false
        binding.menuHandwritingTest.isSelected = false
        binding.menuQuizTest.isSelected = false

        selectedMenu.isSelected = true
    }
}
