package com.example.mindspace.ui.therapist

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.example.mindspace.GenerativeViewModelFactory
import com.example.mindspace.R
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class TherapistFragment : Fragment() {

    private lateinit var therapistViewModel: TherapistViewModel
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: ImageButton

    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_therapist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        therapistViewModel = ViewModelProvider(this, GenerativeViewModelFactory).get(TherapistViewModel::class.java)
        recyclerView = view.findViewById(R.id.recyclerViewChat)
        editTextMessage = view.findViewById(R.id.editTextMessage)
        buttonSend = view.findViewById(R.id.buttonSend)

        chatAdapter = ChatAdapter()
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }

        therapistViewModel.uiState.observe(viewLifecycleOwner, Observer { uiState ->
            Log.d("TherapistFragment", "Messages: ${uiState.messages.size}")
            val formattedMessages = uiState.messages.map { message ->
                message.copy(text = formatMessage(message.text))
            }
            chatAdapter.submitList(formattedMessages) {
                recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }
        })

        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            if (message.isNotBlank()) {
                therapistViewModel.sendMessage(message)
                editTextMessage.text.clear()
                hideKeyboard() // Hide the keyboard after sending the message
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun formatMessage(message: String): String {
        return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY).toString()
    }
}
