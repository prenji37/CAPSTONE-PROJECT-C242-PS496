package com.example.mindspace.questionnaire.questions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.mindspace.R
import com.example.mindspace.databinding.FragmentPageTwoBinding
import com.example.mindspace.ml.MLModelHelper
import com.example.mindspace.questionnaire.QuestionnaireViewModel

class PageTwoFragment : Fragment() {
    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private var _binding: FragmentPageTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mlModelHelper: MLModelHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPageTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlModelHelper = MLModelHelper(requireContext())

        binding.previousButton.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
            viewPager?.currentItem = 0
        }

        binding.submitButton.setOnClickListener {
            val isolation = binding.isolation.progress + 1
            val futureInsecurity = binding.futureInsecurity.progress + 1

            viewModel.setIsolation(isolation)
            viewModel.setFutureInsecurity(futureInsecurity)

            val selectedCgpaId = binding.cgpaRadioGroup.checkedRadioButtonId
            val selectedCgpaButton = view.findViewById<RadioButton>(selectedCgpaId)
            val selectedCgpa = selectedCgpaButton.text.toString()

            val selectedSleepId = binding.averageSleepRadioGroup.checkedRadioButtonId
            val selectedSleepButton = view.findViewById<RadioButton>(selectedSleepId)
            val selectedSleep = selectedSleepButton.text.toString()

            viewModel.setCgpa(selectedCgpa)
            viewModel.setAverageSleep(selectedSleep)

            val output = processAndRunModel()

            Log.d("PageTwoFragment", "Model Output: $output")

            // Show the output using a Toast
            Toast.makeText(requireContext(), "Model Output: $output", Toast.LENGTH_LONG).show()

            // Pass the output data back to the MentalHealthFragment
            val intent = Intent()
            intent.putExtra("model_output", output)
            activity?.setResult(AppCompatActivity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun processAndRunModel(): String {
        val inputData = FloatArray(18) // Adjust size based on the model's input requirements
        inputData[0] = viewModel.studySatisfaction.value?.toFloat() ?: 0.0f
        inputData[1] = viewModel.academicPressure.value?.toFloat() ?: 0.0f
        inputData[2] = viewModel.financialConcerns.value?.toFloat() ?: 0.0f
        inputData[3] = viewModel.socialRelationships.value?.toFloat() ?: 0.0f
        inputData[4] = viewModel.depression.value?.toFloat() ?: 0.0f
        inputData[5] = viewModel.anxiety.value?.toFloat() ?: 0.0f
        inputData[6] = viewModel.isolation.value?.toFloat() ?: 0.0f
        inputData[7] = viewModel.futureInsecurity.value?.toFloat() ?: 0.0f

        val cgpaMap = mapOf(
            "0.0-1.5" to 8,
            "1.5-2.0" to 9,
            "2.0-2.5" to 10,
            "2.5-3.0" to 11,
            "3.0-3.5" to 12,
            "3.5-4.0" to 13
        )
        cgpaMap.forEach { (key, index) ->
            inputData[index] = if (viewModel.cgpa.value == key) 1.0f else 0.0f
        }

        val sleepMap = mapOf(
            "2-4 hrs" to 14,
            "4-6 hrs" to 15,
            "6-7 hrs" to 16,
            "7-8 hrs" to 17
        )
        sleepMap.forEach { (key, index) ->
            inputData[index] = if (viewModel.averageSleep.value == key) 1.0f else 0.0f
        }

        val output = mlModelHelper.runInference(inputData)
        return output
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
