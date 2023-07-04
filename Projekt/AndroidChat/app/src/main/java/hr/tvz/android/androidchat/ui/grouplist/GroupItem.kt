package hr.tvz.android.androidchat.ui.grouplist

import java.util.Date

data class GroupItem(
    val id: Int,
    val name: String,
    val lastMessage: String?,
    val lastMessageTime: String?
)