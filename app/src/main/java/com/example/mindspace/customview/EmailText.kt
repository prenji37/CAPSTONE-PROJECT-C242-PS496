package com.example.mindspace.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mindspace.R

class EmailText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (!isValidEmail(it.toString())) {
                        error = "Invalid email format"
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }
        })

        // Ensure the text color is set correctly
        if (currentTextColor == 0) { // Check if no color is set
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Regex pattern to validate email format
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}
