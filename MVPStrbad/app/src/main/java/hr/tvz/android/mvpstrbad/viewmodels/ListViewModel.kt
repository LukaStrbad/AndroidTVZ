package hr.tvz.android.mvpstrbad.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.tvz.android.mvpstrbad.BuildConfig
import hr.tvz.android.mvpstrbad.model.Picture
import hr.tvz.android.mvpstrbad.webserver.WebServerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.awaitResponse

class ListViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .build()
            val service = retrofit.create(WebServerService::class.java)

            val response = service.getAllPictures().awaitResponse()
            if (!response.isSuccessful) {
                return@launch
            }

            val body = response.body() ?: return@launch

            viewModelScope.launch {
                pictures.value = Json.decodeFromString<List<Picture>>(body.string())
            }
        }
    }

    val pictures = MutableLiveData(listOf<Picture>())

    companion object {
        var API_URL = BuildConfig.API_URL
    }
}