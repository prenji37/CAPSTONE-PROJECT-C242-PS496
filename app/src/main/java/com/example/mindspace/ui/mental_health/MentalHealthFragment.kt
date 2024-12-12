package com.example.mindspace.ui.mental_health

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mindspace.databinding.FragmentMentalHealthBinding
import com.example.mindspace.questionnaire.QuestionnaireActivity

class MentalHealthFragment : Fragment() {

    companion object {
        fun newInstance() = MentalHealthFragment()
    }

    private val viewModel: MentalHealthViewModel by viewModels()
    private var _binding: FragmentMentalHealthBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val output = result.data?.getStringExtra("model_output")
                Log.d("MentalHealthFragment", "Received Model Output: $output")
                if (!output.isNullOrEmpty()) {
                    viewModel.saveOutput(output)
                    Toast.makeText(requireContext(), "Output saved successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "No output received", Toast.LENGTH_LONG).show()
                }
            } else {
                Log.d("MentalHealthFragment", "No output received or result code mismatch")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMentalHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe ViewModel LiveData
        viewModel.outputText.observe(viewLifecycleOwner, Observer { output ->
            binding.outputTextView.text = output
            binding.outputTextView.visibility = if (output.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        viewModel.resultExplanationText.observe(viewLifecycleOwner, Observer { explanation ->
            binding.resultExplanationTextView.text = explanation
            binding.resultExplanationTextView.visibility = if (explanation.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        viewModel.lastTestDateText.observe(viewLifecycleOwner, Observer { dateText ->
            binding.lastTestDateTextView.text = dateText
            binding.lastTestDateTextView.visibility = if (dateText.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        binding.start.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            launcher.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
