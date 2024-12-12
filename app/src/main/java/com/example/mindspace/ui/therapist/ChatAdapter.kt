package com.example.mindspace.ui.therapist

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mindspace.R

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = getItem(position)
        holder.bind(chatMessage)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewMessage)

        fun bind(chatMessage: ChatMessage) {
            // Set the HTML-formatted text
            textView.text = Html.fromHtml(chatMessage.text, Html.FROM_HTML_MODE_LEGACY)

            // Set background based on participant type
            when (chatMessage.participant) {
                Participant.USER -> textView.setBackgroundResource(R.drawable.message_bubble_user)
                Participant.MODEL -> textView.setBackgroundResource(R.drawable.message_bubble_model)
                Participant.ERROR -> textView.setBackgroundResource(R.drawable.message_bubble_error)
            }
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
