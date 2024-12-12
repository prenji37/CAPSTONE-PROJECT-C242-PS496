package com.example.mindspace.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mindspace.R
import com.example.mindspace.databinding.FragmentMeditationBinding

class MeditationFragment : Fragment() {
    private var _binding: FragmentMeditationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeditationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)
        val musicFiles = listOf(R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4, R.raw.music5)
        val adapter = ColorRecyclerAdapter(requireActivity(), images, musicFiles)

        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
