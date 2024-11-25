package com.example.mindspace.ui.mood_tracker

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindspace.R

class MoodTrackerFragment : Fragment() {

    companion object {
        fun newInstance() = MoodTrackerFragment()
    }

    private val viewModel: MoodTrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_mood_tracker, container, false)
    }
}