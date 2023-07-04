package hr.tvz.android.androidchat.viewmodel

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.tvz.android.androidchat.api.AuthService
import hr.tvz.android.androidchat.api.GroupService
import hr.tvz.android.androidchat.api.MessagesService
import hr.tvz.android.androidchat.model.FormMessage
import hr.tvz.android.androidchat.model.Message
import hr.tvz.android.androidchat.model.User
import hr.tvz.android.androidchat.ui.chat.OnRemoveMemberClick
import hr.tvz.android.androidchat.ui.grouplist.GroupItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authService: AuthService,
    private val messagesService: MessagesService,
    private val groupService: GroupService
) : ViewModel(), OnRemoveMemberClick {
    lateinit var profile: User
    var groupItem: GroupItem? = null
    var amIOwner = false

    private val _messages = MutableLiveData<MutableList<Message>>()
    val messages: LiveData<MutableList<Message>> = _messages

    val clearMessage = MutableLiveData(false)

    private var newMessage = ""

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authService.getProfile()

            if (response.body() == null) {
                return@launch
            }

            profile = response.body()!!

            refreshMessages()
        }

        viewModelScope.launch {
            while (true) {
                try {
                    refreshLoop()
                } catch (_: Exception) {
                }
            }
        }
    }

    private suspend fun refreshLoop() {
        delay(1000)

        if (groupItem == null) {
            return
        }

        val lastTime =
            messages.value?.maxBy { it.time }?.time ?: "0"
        val newLastTime =
            runBlocking { messagesService.lastMessageTimeInGroup(groupItem!!.id) }.body()
                ?: "0"

        if (newLastTime > lastTime) {
            refreshMessages()
        }
    }

    fun refreshProfile() = viewModelScope.launch(Dispatchers.IO) {
        val response = authService.getProfile()

        if (response.body() == null) {
            return@launch
        }

        profile = response.body()!!
    }

    fun refreshMessages() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (groupItem == null) {
                return@launch
            }
            val newMessages = messagesService.getAllMessages(groupItem!!.id)
            _messages.postValue(newMessages.body()?.toMutableList())
            amIOwner = groupService.amIOwner(groupItem!!.id).body() == true
        } catch (_: Exception) {
        }
    }

    fun setNewMessage(newMessage: Editable) {
        if (this.newMessage != newMessage.toString()) {
            this.newMessage = newMessage.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun sendMessage() = viewModelScope.launch(Dispatchers.IO) {
        if (groupItem == null || newMessage.isBlank()) {
            return@launch
        }

        messagesService.send(
            FormMessage(
                groupItem!!.id,
                newMessage
            )
        )
        refreshMessages()
        clearMessage.postValue(true)
    }

    suspend fun getMembers(): List<User> {
        groupItem?.let {
            return groupService.getMembers(it.id).body() ?: listOf()
        }

        return listOf()
    }

    suspend fun getUsers(): List<User> {
        val allUsers = authService.getUsers().body()?.toMutableList() ?: mutableListOf()
        getMembers().let { members ->
            members.forEach { member ->
                allUsers.removeIf { u -> u.username == member.username }
            }
        }

        return allUsers
    }

    fun addUserToGroup(user: User) = viewModelScope.launch(Dispatchers.IO) {
        groupService.addMember(groupItem!!.id, user.username)
    }

    override fun onRemoveMemberClick(member: User) {
        if (groupItem == null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            groupService.removeMember(groupItem!!.id, member.username)
        }
    }
}