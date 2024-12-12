package com.example.mindspace.questionnaire

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mindspace.questionnaire.questions.PageOneFragment
import com.example.mindspace.questionnaire.questions.PageTwoFragment

class QuestionnairePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PageOneFragment()
            1 -> PageTwoFragment()
            // Add more pages as needed
            else -> PageOneFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // Total number of pages
    }
}
