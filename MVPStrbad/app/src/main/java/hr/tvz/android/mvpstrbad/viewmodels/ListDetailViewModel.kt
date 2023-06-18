package hr.tvz.android.mvpstrbad.viewmodels

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import hr.tvz.android.mvpstrbad.PictureViewActivity
import hr.tvz.android.mvpstrbad.R
import hr.tvz.android.mvpstrbad.model.Picture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListDetailViewModel(application: Application, savedInstanceState: SavedStateHandle) :
    AndroidViewModel(application) {
    val picture = savedInstanceState.get<Picture>("picture")

    val title = picture?.title
    val description = picture?.description
    val image = picture?.image
    private val webLink = picture?.webLink

    private val _shareState = MutableStateFlow(false)
    val shareState = _shareState.asStateFlow()

    private var mediaPlayer = MediaPlayer.create(getApplication(), R.raw.song)

    fun onPictureClick() {
        val intent = Intent(getApplication(), PictureViewActivity::class.java)
        intent.putExtra("picture", picture).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getApplication<Application>().startActivity(intent)
    }

    fun onLinkClick() {
        val browserIntent = Intent(Intent.ACTION_VIEW, webLink?.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getApplication<Application>().startActivity(browserIntent)
    }

    fun onShareClick() {
        _shareState.update { true }
        _shareState.update { false }
    }

    fun onPlayClick() {
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.song)
        mediaPlayer.start()
    }

    fun onPauseClick() {
        mediaPlayer.stop()
    }
}