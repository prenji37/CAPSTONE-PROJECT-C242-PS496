package com.example.mindspace.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _moodData = MutableLiveData<String>()
    val moodData: LiveData<String> get() = _moodData

    private val _lastTestDate = MutableLiveData<String>()
    val lastTestDate: LiveData<String> get() = _lastTestDate

    private val _testResult = MutableLiveData<String>()
    val testResult: LiveData<String> get() = _testResult

    private val _testReminder = MutableLiveData<Boolean>()
    val testReminder: LiveData<Boolean> get() = _testReminder

    init {
        checkTodayMood()
        fetchLastTestDateAndResult()
    }

    private fun checkTodayMood() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(calendar.time)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("moods").child(today)

        databaseReference.get().addOnSuccessListener { snapshot ->
            val moodForToday = snapshot.child("mood").value?.toString() ?: "Submit your mood today!"
            _moodData.value = if (moodForToday != "null") {
                "Today's Mood: $moodForToday"
            } else {
                "Submit your mood today!"
            }
        }
    }

    private fun fetchLastTestDateAndResult() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("mental_health_results")

        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            val output = dataSnapshot.child("output").getValue(String::class.java) ?: "No result found."
            val timestamp = dataSnapshot.child("timestamp").getValue(Long::class.java)

            if (timestamp != null) {
                val lastTestDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
                _lastTestDate.value = "Last Test: $lastTestDateString"

                val daysSinceLastTest = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60 * 24)
                _testReminder.value = daysSinceLastTest > 30

                _testResult.value = output
            } else {
                _lastTestDate.value = "No test taken yet."
                _testReminder.value = true
                _testResult.value = "No result found."
            }
        }
    }
}
