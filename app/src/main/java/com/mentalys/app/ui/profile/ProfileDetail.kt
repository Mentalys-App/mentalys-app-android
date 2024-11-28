package com.mentalys.app.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityProfileDetailBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.auth.AuthViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.showToast

class ProfileDetail : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this@ProfileDetail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.profileLogoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.profileEditTextView.setOnClickListener {
            val bottomSheet = ProfileEditBottomSheet()
            val name = binding.profileFullNameTextView.text.toString()
            val username = binding.profileUsernameTextView.text.toString()
            val email = binding.profileEmailTextView.text.toString()
            val dob = binding.profileDobTextView.text.toString()
            val gender = binding.profileGenderTextView.text.toString()
            val location = binding.profileLocationTextView.text.toString()

            // Pass existing profile data as arguments
            bottomSheet.arguments = Bundle().apply {
                putString("name", name)
                putString("username", username)
                putString("email", email)
                putString("dob", dob)
                putString("gender", gender)
                putString("location", location)
            }

            bottomSheet.onProfileUpdated = { email, dob, gender, location ->
                // Update your profile data here
                showToast(this@ProfileDetail, email + dob + gender + location)
            }

            bottomSheet.show(supportFragmentManager, "ProfileEditBottomSheet")
        }

    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteLoginSession()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}