package com.mentalys.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMainBinding
import com.mentalys.app.databinding.ActivityMentalCheckBinding

class MentalCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentalCheckBinding
    private var selectedMenu: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test voice
        val menuVoice = binding.menuVoiceTest
        menuVoice.setOnClickListener {
            setSelectedMenu(menuVoice)
        }

        // test handwriting
        val menuHandWriting = binding.menuHandwritingTest
        menuHandWriting.setOnClickListener {
            setSelectedMenu(menuHandWriting)
        }

        // test quiz
        val menuQuiz = binding.menuQuizTest
        menuQuiz.setOnClickListener {
            setSelectedMenu(menuQuiz)
        }

        binding.startTestBtn.setOnClickListener {
            when (selectedMenu) {
                binding.menuVoiceTest -> startActivity(Intent(this, VoiceTestActivity::class.java))
                binding.menuHandwritingTest -> startActivity(Intent(this, HandwritingTestActivity::class.java))
                binding.menuQuizTest -> startActivity(Intent(this, QuizTestActivity::class.java))
                else -> Toast.makeText(this, "Pilih menu terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setSelectedMenu(menu: LinearLayout) {
        binding.menuVoiceTest.isSelected = false
        binding.menuHandwritingTest.isSelected = false
        binding.menuQuizTest.isSelected = false

        menu.isSelected = true
        selectedMenu = menu
    }
}
