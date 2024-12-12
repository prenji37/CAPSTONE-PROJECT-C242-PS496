package com.example.mindspace.ui.mental_health

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MentalHealthViewModel(application: Application) : AndroidViewModel(application) {

    private val _outputText = MutableLiveData<String?>()
    val outputText: LiveData<String?> get() = _outputText

    private val _resultExplanationText = MutableLiveData<String?>()
    val resultExplanationText: LiveData<String?> get() = _resultExplanationText

    private val _lastTestDateText = MutableLiveData<String?>()
    val lastTestDateText: LiveData<String?> get() = _lastTestDateText

    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchExistingData()
    }

    fun saveOutput(output: String?) {
        if (uid != null && output != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("mental_health_results")
            val resultMap = mapOf(
                "output" to output,
                "timestamp" to System.currentTimeMillis()
            )
            databaseReference.setValue(resultMap)
            _outputText.value = output
            _resultExplanationText.value = getResultExplanation(output)

            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            _lastTestDateText.value = "Last Test Taken: ${dateFormat.format(Date(System.currentTimeMillis()))}"
        }
    }

    private fun fetchExistingData() {
        if (uid != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("mental_health_results")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val output = dataSnapshot.child("output").getValue(String::class.java)
                    val timestamp = dataSnapshot.child("timestamp").getValue(Long::class.java)

                    if (!output.isNullOrEmpty()) {
                        _outputText.value = output
                        _resultExplanationText.value = getResultExplanation(output)

                        if (timestamp != null) {
                            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                            _lastTestDateText.value = "Last Test Taken: ${dateFormat.format(Date(timestamp))}"
                        }
                    } else {
                        _outputText.value = "You haven't taken the test yet. Please complete the questionnaire to get your results."
                        _resultExplanationText.value = "You haven't taken the test yet. Please complete the questionnaire to get your results."
                        _lastTestDateText.value = null
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun getResultExplanation(output: String): String {
        // Extracting the prediction from the output string
        val prediction = output.replace("Prediction: ", "").trim()
        return when (prediction) {
            "Mild" -> "Mild: Your mental health is generally in a good state, but it's always beneficial to maintain a balanced lifestyle and keep monitoring."
            "Moderate" -> "Moderate: You may be experiencing some challenges. Consider implementing some coping strategies and possibly seeking guidance from a mental health professional."
            "Severe" -> "Severe: It’s important to address your mental health with seriousness. Seeking professional help and support from loved ones can make a significant difference."
            "Critical" -> "Critical: Immediate action is necessary. Please reach out to mental health services or professionals for urgent support."
            else -> "You haven't taken the test yet. Please complete the questionnaire to get your results."
        }
    }
}
