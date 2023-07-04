package hr.tvz.android.androidchat.ui.chat

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.androidchat.R
import hr.tvz.android.androidchat.databinding.ChatItemBinding
import hr.tvz.android.androidchat.model.Message
import hr.tvz.android.androidchat.model.User

class ChatAdapter(
    val messages: MutableList<Message>,
    private val currentUser: User,
    private val context: Context
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ChatViewHolder(binding)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages.sortedBy { it.time }[messages.size - position - 1]

        holder.binding.run {
            item = message

            // If message is sent by current user align message to right
            if (currentUser.username == message.senderUsername) {
                leftSpace.visibility = View.VISIBLE
                rightSpace.visibility = View.GONE
                messageCard.setCardBackgroundColor(getColor(true))
            } else {
                leftSpace.visibility = View.GONE
                rightSpace.visibility = View.VISIBLE
                messageCard.setCardBackgroundColor(getColor(false))
            }
        }
    }

    private fun getColor(primary: Boolean): Int {
        val value = TypedValue()
        val attr = if (primary) {
            com.google.android.material.R.attr.colorPrimaryContainer
        } else {
            com.google.android.material.R.attr.colorSecondaryContainer
        }
        context.theme.resolveAttribute(attr, value, true)
        return value.data
    }
}