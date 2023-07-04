package hr.tvz.android.androidchat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.tvz.android.androidchat.api.GroupService
import hr.tvz.android.androidchat.api.MessagesService
import hr.tvz.android.androidchat.ui.grouplist.GroupItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val groupService: GroupService,
    private val messagesService: MessagesService
) : ViewModel() {
    private val _groupItems = MutableLiveData<List<GroupItem>>()
    val groupItems = _groupItems

    init {
        refreshGroups()

        viewModelScope.launch {
            while (true) {
                delay(1000)

                refreshGroups()
//                val lastTime =
//                    groupItems.value?.maxBy { it.lastMessageTime ?: "0" }?.lastMessageTime ?: "0"
//                val newLastTime = runBlocking { messagesService.lastMessageTime() }.body() ?: "0"
//
//                if (newLastTime > lastTime) {
//                    refreshGroups()
//                }
            }
        }
    }

    fun refreshGroups() = viewModelScope.launch(Dispatchers.IO) {
        val response = groupService.getGroups()

        if (response.isSuccessful && response.body() != null) {
            val groups = response.body()!!
            val groupItems = groups.map {
                val lastMessage = messagesService.lastMessage(it.id).body()
                GroupItem(
                    it.id,
                    it.name,
                    lastMessage?.content,
                    lastMessage?.time
                )
            }
            _groupItems.postValue(groupItems.sortedBy { it.lastMessageTime }.reversed())
        }
    }
}