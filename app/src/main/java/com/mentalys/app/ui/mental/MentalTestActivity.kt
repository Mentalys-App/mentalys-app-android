package com.mentalys.app.ui.mental

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mentalys.app.databinding.ActivityMentalTestBinding
import com.mentalys.app.ui.mental.test.handwriting.MentalTestHandwritingActivity
import com.mentalys.app.ui.mental.test.quiz.MentalTestQuizTestActivity
import com.mentalys.app.ui.mental.test.voice.MentalTestVoiceActivity
import com.mentalys.app.utils.showToast

class MentalTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMentalTestBinding
    private var selectedMenu: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // test voice
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
                binding.menuVoiceTest -> startActivity(Intent(this, MentalTestVoiceActivity::class.java))
                binding.menuHandwritingTest -> startActivity(Intent(this, MentalTestHandwritingActivity::class.java))
                binding.menuQuizTest -> startActivity(Intent(this, MentalTestQuizTestActivity::class.java))
                else -> showToast(this, "Pilih menu terlebih dahulu")
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
