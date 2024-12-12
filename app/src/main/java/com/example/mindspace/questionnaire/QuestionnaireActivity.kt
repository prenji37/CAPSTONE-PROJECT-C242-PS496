package com.example.mindspace.questionnaire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mindspace.R
import com.example.mindspace.questionnaire.QuestionnairePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class QuestionnaireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val pagerAdapter = QuestionnairePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Page ${position + 1}"
        }.attach()
    }
}
