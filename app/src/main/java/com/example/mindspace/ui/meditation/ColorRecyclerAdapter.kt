package com.example.mindspace.ui.meditation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ColorRecyclerAdapter(activity: FragmentActivity, private val images: List<Int>, private val musicFiles: List<Int>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3 // Simulate infinite items

    override fun createFragment(position: Int): Fragment {
        return ColorPageFragment.newInstance(images[position], musicFiles[position])
    }
}
