package com.mentalys.app.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mentalys.app.R

class CustomRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    var errorMessage: String? = null
    private var errorTextView: TextView? = null

    init {
        orientation = VERTICAL


        errorTextView = TextView(context).apply {
            setTextColor(ContextCompat.getColor(context, R.color.error_color))
            visibility = GONE
        }
        addView(errorTextView)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomRadioGroup,
            0,
            0
        ).apply {
            try {
                errorMessage = getString(R.styleable.CustomRadioGroup_errorText)
            } finally {
                recycle()
            }
        }
    }

    fun validateSelection(): Boolean {
        return if (checkedRadioButtonId == -1) {
            errorMessage?.let {
                errorTextView?.text = it
                errorTextView?.visibility = VISIBLE
            }
            false
        } else {
            errorTextView?.visibility = GONE
            true
        }
    }

    fun clearValidationError() {
        errorTextView?.visibility = GONE
    }
}