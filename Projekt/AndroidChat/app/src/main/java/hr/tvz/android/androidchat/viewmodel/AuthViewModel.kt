package hr.tvz.android.androidchat.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.tvz.android.androidchat.api.AuthService
import hr.tvz.android.androidchat.api.GroupService
import hr.tvz.android.androidchat.api.TokenManager
import hr.tvz.android.androidchat.model.Auth
import hr.tvz.android.androidchat.model.AuthRegister
import hr.tvz.android.androidchat.model.GroupForm
import hr.tvz.android.androidchat.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val groupService: GroupService,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn = _loggedIn
    private val _profile = MutableLiveData<User>()
    val profile = _profile
    private val _showRegister = MutableLiveData(false)
    val showRegister: LiveData<Boolean> = _showRegister

    var displayName = ""
    var username = ""
    var password = ""

    fun checkLoggedIn() = viewModelScope.launch(Dispatchers.IO) {
        val response = authService.getProfile()

        if (response.isSuccessful) {
            _loggedIn.postValue(true)
            _profile.postValue(response.body())
        }
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {
        val response = authService.login(Auth(username, password))


        response.body()?.let {
            tokenManager.saveToken(it.token)
            _loggedIn.postValue(response.isSuccessful)
        }
    }

    fun register() = viewModelScope.launch(Dispatchers.IO) {
        val response = authService.register(
            AuthRegister(
                displayName,
                username,
                password
            )
        )


        response.body()?.let {
            tokenManager.saveToken(it.token)
            _loggedIn.postValue(response.isSuccessful)
        }
    }

    suspend fun createGroup(name: String): Boolean {
        val response = groupService.createGroup(GroupForm(name))

        return response.isSuccessful
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        tokenManager.deleteToken()
        _loggedIn.postValue(false)
    }

    fun showRegister() {
        _showRegister.value = !(_showRegister.value ?: false)
    }

    fun setDisplayName(displayName: Editable) {
        this.displayName = displayName.toString()
    }

    fun setUsername(username: Editable) {
        this.username = username.toString()
    }

    fun setPassword(password: Editable) {
        this.password = password.toString()
    }
}