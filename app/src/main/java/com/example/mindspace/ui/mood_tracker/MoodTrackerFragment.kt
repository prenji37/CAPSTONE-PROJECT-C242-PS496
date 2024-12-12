package com.example.mindspace.ui.mood_tracker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mindspace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MoodTrackerFragment : Fragment() {

    private lateinit var viewModel: MoodTrackerViewModel
    private lateinit var weeklySummaryText: TextView
    private val daysOfWeek = listOf("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weeklySummaryText = view.findViewById(R.id.weeklySummaryText)
        viewModel = ViewModelProvider(this).get(MoodTrackerViewModel::class.java)

        val moodButtons = listOf(
            view.findViewById<Button>(R.id.happyButton),
            view.findViewById<Button>(R.id.contentButton),
            view.findViewById<Button>(R.id.neutralButton),
            view.findViewById<Button>(R.id.sadButton),
            view.findViewById<Button>(R.id.anxiousButton),
            view.findViewById<Button>(R.id.angryButton)
        )
        val alreadySubmittedText: TextView = view.findViewById(R.id.alreadySubmittedText)
        val weeklyViews = mapOf(
            "sunday" to view.findViewById<View>(R.id.sundayMood),
            "monday" to view.findViewById<View>(R.id.mondayMood),
            "tuesday" to view.findViewById<View>(R.id.tuesdayMood),
            "wednesday" to view.findViewById<View>(R.id.wednesdayMood),
            "thursday" to view.findViewById<View>(R.id.thursdayMood),
            "friday" to view.findViewById<View>(R.id.fridayMood),
            "saturday" to view.findViewById<View>(R.id.saturdayMood)
        )

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(calendar.time)

        viewModel.moodData.observe(viewLifecycleOwner, Observer { moodMap ->
            moodMap.forEach { (date, mood) ->
                calendar.time = dateFormat.parse(date)!!
                val dayOfWeek = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                val dayView = weeklyViews[dayOfWeek]

                if (dayView != null) {
                    val background = dayView.background.mutate() as GradientDrawable
                    background.setColor(getMoodColor(mood))
                }

                if (date == today && mood != "No mood") {
                    moodButtons.forEach { it.visibility = View.GONE }
                    alreadySubmittedText.visibility = View.VISIBLE
                }
            }
        })

        viewModel.weeklySummary.observe(viewLifecycleOwner, Observer { summary ->
            weeklySummaryText.text = summary
        })

        moodButtons.forEach { button ->
            button.setOnClickListener {
                if (isNetworkAvailable(requireContext())) {
                    val selectedMood = button.text.toString()
                    saveMood(today, selectedMood)
                    Toast.makeText(context, "Mood saved!", Toast.LENGTH_SHORT).show()
                    moodButtons.forEach { it.visibility = View.GONE }
                    alreadySubmittedText.visibility = View.VISIBLE
                    viewModel.fetchMoodData(requireContext()) // Refresh data after submission
                } else {
                    // Show error message
                    Toast.makeText(requireContext(), "Network not detected. Please check your connection and try again.", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Fetch mood data with context
        viewModel.fetchMoodData(requireContext())
    }

    private fun saveMood(date: String, mood: String) {
        // Get the current user's UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return  // Return if no user is logged in

        // Save mood data under the user's UID
        val moodData = mapOf("mood" to mood, "timestamp" to System.currentTimeMillis())
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(uid)
            .child("moods")
            .child(date)
            .setValue(moodData)
    }

    private fun getMoodColor(mood: String): Int {
        return when (mood) {
            "Happy" -> Color.parseColor("#FFEB3B")
            "Content" -> Color.parseColor("#8BC34A")
            "Neutral" -> Color.parseColor("#00BCD4")
            "Sad" -> Color.parseColor("#FF5722")
            "Anxious" -> Color.parseColor("#9C27B0")
            "Angry" -> Color.parseColor("#F44336")
            "Excited" -> Color.parseColor("#FFC107")
            else -> Color.GRAY
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
