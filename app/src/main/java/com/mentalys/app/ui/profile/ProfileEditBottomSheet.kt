package com.mentalys.app.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mentalys.app.R
import com.mentalys.app.databinding.LayoutBottomSheetEditProfileBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileEditBottomSheet: BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetEditProfileBinding? = null
    private val binding get() = _binding!!
    var onProfileUpdated: ((String, String, String, String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Set initial values (pass data using arguments)
            val args = arguments
            etName.setText(args?.getString("name"))
            etUsername.setText(args?.getString("username"))
            etEmail.setText(args?.getString("email"))
            etDob.setText(args?.getString("dob"))
            etLocation.setText(args?.getString("location"))
            when (args?.getString("gender")) {
                "Male" -> rgGender.check(R.id.rb_male)
                "Female" -> rgGender.check(R.id.rb_female)
            }

            // Handle DatePickerDialog for DOB EditText
            etDob.setOnClickListener {
                showDatePicker(etDob)
            }

            // Handle submit button click
            btnSubmit.setOnClickListener {
                val email = etEmail.text.toString()
                val dob = etDob.text.toString()
                val gender = when (rgGender.checkedRadioButtonId) {
                    R.id.rb_male -> "Male"
                    R.id.rb_female -> "Female"
                    else -> ""
                }
                val location = etLocation.text.toString()

                onProfileUpdated?.invoke(email, dob, gender, location) // Pass the data to the parent activity/fragment
                dismiss() // Close the bottom sheet
            }
        }

    }

    // Function to show the DatePickerDialog
    private fun showDatePicker(dobEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Use SimpleDateFormat to format the selected date
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)
                dobEditText.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = Calendar.getInstance().apply {
            set(1900, Calendar.JANUARY, 1)
        }.timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

}