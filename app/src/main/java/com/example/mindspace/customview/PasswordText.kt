package com.example.mindspace.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mindspace.R

class PasswordText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var isPasswordVisible: Boolean = false
    private var visibilityToggleDrawable: Drawable? = null

    init{
        visibilityToggleDrawable = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off)
        setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityToggleDrawable, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (!isPasswordValid(it.toString())) {
                        setError("Password must be at least 8 characters long")
                    } else {
                        setError(null)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }
        })

        setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility()
                    performClick()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    override fun setError(error: CharSequence?){
        super.setError(error, null)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        Log.d("PasswordText", "Password visibility toggled: $isPasswordVisible")

        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        visibilityToggleDrawable = ContextCompat.getDrawable(
            context,
            if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
        )

        visibilityToggleDrawable?.setBounds(0, 0, visibilityToggleDrawable!!.intrinsicWidth, visibilityToggleDrawable!!.intrinsicHeight)
        setCompoundDrawables(null, null, visibilityToggleDrawable, null)

        invalidate()
        setSelection(text?.length ?: 0)  // Move cursor to the end
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^.{8,}$"
        return password.matches(passwordRegex.toRegex())
    }
}