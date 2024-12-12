package com.example.mindspace.ui.therapist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class TherapistViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private var chat: Chat? = null
    private val databaseReference: DatabaseReference
    private val uid: String

    init {
        // Initialize Firebase Auth and Database Reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        uid = currentUser?.uid ?: throw IllegalArgumentException("User not logged in")
        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("mental_health_results")

        // Fetch data from Firebase and initialize chat
        fetchResultFromFirebase()
    }

    private fun startChatOption1() = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Hi, I've been feeling a bit overwhelmed lately.") },
            content(role = "model") { text("Hi there! I’m here to lend an ear. It sounds like things have been a little challenging for you. Do you want to talk about what’s been on your mind recently? Maybe we can figure out a few small steps to help you feel better.") }
        )
    )

    private fun startChatOption2() = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Hi, I've been feeling quite overwhelmed lately.") },
            content(role = "model") { text("Hi there, I'm here to help. I’m sorry to hear that you’re feeling overwhelmed. It can be tough to manage everything when it feels like it's all piling up. Would you like to share more about what’s been causing you to feel this way? Sometimes talking about it can really help.") }
        )
    )

    private fun startChatOption3() = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Hi, I've been feeling very overwhelmed and stressed lately.") },
            content(role = "model") { text("Hi there, I'm here to support you. It sounds like you’re really going through a difficult time. Stress and overwhelm can make everything feel so heavy. Let’s take a moment together to unpack what you’re dealing with. What’s been on your mind the most?") }
        )
    )

    private fun startChatOption4() = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Hi, I'm feeling extremely overwhelmed and in a dark place.") },
            content(role = "model") { text("Hi there, I'm here for you. It sounds like you’re in a very tough spot right now. I want to make sure you know that you’re not alone in this. Please, share with me what’s been happening. We can take it one step at a time and find a way through this together.") }
        )
    )

    private val _uiState: MutableLiveData<ChatUiState> = MutableLiveData()
    val uiState: LiveData<ChatUiState> = _uiState

    private fun fetchResultFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val output = dataSnapshot.child("output").getValue(String::class.java)
                if (!output.isNullOrEmpty()) {
                    initializeChatBasedOnResult(output)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun initializeChatBasedOnResult(result: String) {
        chat = when {
            result.contains("Mild") -> startChatOption1()
            result.contains("Moderate") -> startChatOption2()
            result.contains("Severe") -> startChatOption3()
            result.contains("Critical") -> startChatOption4()
            else -> generativeModel.startChat(
                history = listOf(
                    content(role = "user") { text("Hi, I've been feeling quite overwhelmed lately.") },
                    content(role = "model") { text("Hi there, I'm here to help. How are you feeling today?") }
                )
            )
        }
        updateUIState()
    }

    private fun updateUIState() {
        chat?.let {
            _uiState.value = ChatUiState(it.history.map { content ->
                ChatMessage(
                    text = content.parts.first().asTextOrNull() ?: "",
                    participant = if (content.role == "user") Participant.USER else Participant.MODEL,
                    isPending = false
                )
            })
        }
    }

    fun sendMessage(userMessage: String) {
        _uiState.value?.let { currentState ->
            val newMessages = currentState.messages.toMutableList().apply {
                add(
                    ChatMessage(
                        text = userMessage,
                        participant = Participant.USER,
                        isPending = true
                    )
                )
            }
            _uiState.value = ChatUiState(newMessages)
        }

        viewModelScope.launch {
            try {
                chat?.let {
                    val response = it.sendMessage(userMessage)

                    _uiState.value?.replaceLastPendingMessage()
                    _uiState.postValue(_uiState.value) // Trigger LiveData update

                    response.text?.let { modelResponse ->
                        _uiState.value?.addMessage(
                            ChatMessage(
                                text = formatMessage(modelResponse), // Ensure the message is formatted
                                participant = Participant.MODEL,
                                isPending = false
                            )
                        )
                        _uiState.postValue(_uiState.value) // Trigger LiveData update
                    }
                }
            } catch (e: Exception) {
                _uiState.value?.replaceLastPendingMessage()
                _uiState.value?.addMessage(
                    ChatMessage(
                        text = e.localizedMessage,
                        participant = Participant.ERROR
                    )
                )
                _uiState.postValue(_uiState.value) // Trigger LiveData update
            }
        }
    }

    private fun formatMessage(message: String): String {
        // Example: Convert custom syntax to markdown
        var formattedMessage = message
        // Convert **text** to <b>text</b>
        formattedMessage = formattedMessage.replace(Regex("\\*\\*(.+?)\\*\\*"), "<b>$1</b>")
        // Convert *text* to <i>text</i>
        formattedMessage = formattedMessage.replace(Regex("\\*(.+?)\\*"), "<i>$1</i>")
        // Convert lists
        formattedMessage = formattedMessage.replace(Regex("^- (.+?)$", RegexOption.MULTILINE), "<ul><li>$1</li></ul>")
        return formattedMessage
    }

}

