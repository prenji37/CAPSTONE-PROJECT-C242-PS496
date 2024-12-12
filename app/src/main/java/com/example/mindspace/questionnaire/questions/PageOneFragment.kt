package com.example.mindspace.questionnaire.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.mindspace.R
import com.example.mindspace.databinding.FragmentPageOneBinding
import com.example.mindspace.questionnaire.QuestionnaireViewModel

class PageOneFragment : Fragment() {
    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private var _binding: FragmentPageOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener {
            val studySatisfaction = binding.studySatisfaction.progress + 1
            val academicPressure = binding.academicPressure.progress + 1
            val financialConcerns = binding.financialConcerns.progress + 1
            val socialRelationships = binding.socialRelationships.progress + 1
            val depression = binding.depression.progress + 1
            val anxiety = binding.anxiety.progress + 1

            viewModel.setStudySatisfaction(studySatisfaction)
            viewModel.setAcademicPressure(academicPressure)
            viewModel.setFinancialConcerns(financialConcerns)
            viewModel.setSocialRelationships(socialRelationships)
            viewModel.setDepression(depression)
            viewModel.setAnxiety(anxiety)

            val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
            viewPager?.currentItem = 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

