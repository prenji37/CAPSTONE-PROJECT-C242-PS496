package com.example.mindspace.ui.meditation

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageButton
import com.example.mindspace.R
import com.example.mindspace.databinding.ItemPageBinding

class ColorPageFragment : Fragment() {
    private var _binding: ItemPageBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var handler: Handler? = null
    private var playDuration: Long = 30000 // Default 30 seconds

    companion object {
        private const val IMAGE_KEY = "image"
        private const val MUSIC_KEY = "music"

        fun newInstance(imageResId: Int, musicResId: Int): ColorPageFragment {
            val fragment = ColorPageFragment()
            val bundle = Bundle()
            bundle.putInt(IMAGE_KEY, imageResId)
            bundle.putInt(MUSIC_KEY, musicResId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageResId = arguments?.getInt(IMAGE_KEY) ?: 0
        val musicResId = arguments?.getInt(MUSIC_KEY) ?: 0

        binding.imageView.setImageResource(imageResId)
        handler = Handler(Looper.getMainLooper())

        binding.playPauseButton.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, musicResId)
                mediaPlayer?.isLooping = true
            }

            if (isPlaying) {
                stopMusic()
            } else {
                startMusic()
            }
        }

        binding.setTimerButton.setOnClickListener {
            showCustomTimePickerDialog()
        }
    }

    private fun showCustomTimePickerDialog() {
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.setContentView(R.layout.dialog_time_picker)

        val hourPicker = dialog.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = dialog.findViewById<NumberPicker>(R.id.minutePicker)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val okButton = dialog.findViewById<Button>(R.id.okayButton)

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.wrapSelectorWheel = true
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.wrapSelectorWheel = true

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        okButton.setOnClickListener {
            val hours = hourPicker.value
            val minutes = minutePicker.value
            playDuration = (hours * 3600000 + minutes * 60000).toLong()
            binding.durationTextView.text = String.format("Duration: %02d:%02d", hours, minutes)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startMusic() {
        mediaPlayer?.start()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        handler?.postDelayed({
            stopMusic()
        }, playDuration)
    }

    private fun stopMusic() {
        mediaPlayer?.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.ic_play)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler?.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }
}
