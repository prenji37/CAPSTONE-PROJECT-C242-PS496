package com.example.mindspace.ui.mood_tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class MoodTrackerViewModel : ViewModel() {

    private val _moodData = MutableLiveData<Map<String, String>>()
    val moodData: LiveData<Map<String, String>> get() = _moodData

    private val _weeklySummary = MutableLiveData<String>()
    val weeklySummary: LiveData<String> get() = _weeklySummary

    fun fetchMoodData(context: Context) {
        if (!isNetworkAvailable(context)) {
            // Handle no internet connection
            _moodData.value = emptyMap()
            _weeklySummary.value = "No internet connection. Unable to fetch mood data."
            Toast.makeText(context, "No internet connection. Please check your connection and try again.", Toast.LENGTH_LONG).show()
            return
        }

        val database = FirebaseDatabase.getInstance().reference
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentWeek = mutableListOf<String>()
        val moodMap = mutableMapOf<String, String>()

        // Get the current user's UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return  // Return if no user is logged in

        // Get dates for the current week
        for (i in 0..6) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i)
            currentWeek.add(dateFormat.format(calendar.time))
        }

        currentWeek.forEach { date ->
            database.child("users").child(uid).child("moods").child(date).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val mood = snapshot.child("mood").value.toString()
                    moodMap[date] = mood
                } else {
                    moodMap[date] = "No mood"
                }
                if (moodMap.size == currentWeek.size) {
                    _moodData.value = moodMap
                    calculateWeeklySummary(moodMap.values)
                    Log.d("MoodTracker", "Mood Data: $moodMap")
                }
            }.addOnFailureListener { exception ->
                Log.e("MoodTracker", "Failed to retrieve data for date: $date. Exception: ${exception.message}")
            }
        }
    }

    private fun calculateWeeklySummary(moods: Collection<String>) {
        val moodFrequency = moods.groupingBy { it }.eachCount()
        val mostPickedMood = moodFrequency.filter { it.key != "No mood" }
            .maxByOrNull { it.value }?.key ?: "No mood recorded"
        _weeklySummary.value = "Most picked mood: $mostPickedMood"
        Log.d("MoodTracker", "Weekly Summary: Most picked mood is $mostPickedMood")
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
