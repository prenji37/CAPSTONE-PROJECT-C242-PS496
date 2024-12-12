package com.example.mindspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.example.mindspace.ui.therapist.TherapistViewModel

val GenerativeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        viewModelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val config = generationConfig {
            temperature = 0.7f
        }

        return with(viewModelClass) {
            when {
                isAssignableFrom(TherapistViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the `gemini-flash` AI model for chat
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apiKey,
                        generationConfig = config
                    )
                    TherapistViewModel(generativeModel)
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${viewModelClass.name}")
            }
        } as T
    }
}
