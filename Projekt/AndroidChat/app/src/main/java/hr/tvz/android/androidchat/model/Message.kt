package hr.tvz.android.androidchat.model

data class Message(
    val senderUsername: String,
    val senderName: String?,
    val content: String,
    val time: String
)
