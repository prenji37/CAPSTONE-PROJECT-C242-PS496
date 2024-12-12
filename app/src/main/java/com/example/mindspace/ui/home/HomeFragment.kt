package com.example.mindspace.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mindspace.R
import com.example.mindspace.WelcomeActivity
import com.example.mindspace.databinding.FragmentHomeBinding
import com.example.mindspace.questionnaire.QuestionnaireActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val placeholderUsername = "Guest"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var moodStatusText: TextView
    private lateinit var lastTestDateTextView: TextView
    private lateinit var testReminderTextView: TextView
    private lateinit var testResultTextView: TextView
    private lateinit var takeTestButton: Button
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize views
        val usernameTextView: TextView = binding.usernameTextView
        profileImageView = binding.profileImage

        moodStatusText = binding.moodStatusText
        lastTestDateTextView = binding.lastTestDateTextView
        testReminderTextView = binding.testReminderTextView
        takeTestButton = binding.takeTestButton
        testResultTextView = binding.testResultTextView

        usernameTextView.text = "Username: $placeholderUsername"

        binding.logoutButton.setOnClickListener { logoutUser() }
        binding.googleLogoutButton.setOnClickListener { logoutGoogleUser() }

        googleSignInClient = GoogleSignIn.getClient(
            requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )

        // Initialize the ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observe LiveData from ViewModel
        homeViewModel.moodData.observe(viewLifecycleOwner, Observer { mood ->
            moodStatusText.text = mood
        })

        homeViewModel.lastTestDate.observe(viewLifecycleOwner, Observer { date ->
            lastTestDateTextView.text = date
        })

        homeViewModel.testResult.observe(viewLifecycleOwner, Observer { result ->
            testResultTextView.text = result
            testResultTextView.visibility = if (result != "No result found.") View.VISIBLE else View.GONE
        })

        homeViewModel.testReminder.observe(viewLifecycleOwner, Observer { reminder ->
            testReminderTextView.visibility = if (reminder) View.VISIBLE else View.GONE
        })

        takeTestButton.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

        loadProfileImage()

        return root
    }

    private fun loadProfileImage() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            val personPhoto = account.photoUrl
            val email = account.email
            if (personPhoto != null) {
                Glide.with(this).load(personPhoto).into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.ic_person)
            }

            if (email != null) {
                val usernameTextView: TextView = binding.usernameTextView
                usernameTextView.text = "Username: $email"
            }
        }
    }

    private fun logoutUser() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun logoutGoogleUser() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
