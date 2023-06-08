package hr.tvz.android.mvpstrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import hr.tvz.android.mvpstrbad.databinding.ActivityPictureViewBinding
import hr.tvz.android.mvpstrbad.extensions.parcelable
import hr.tvz.android.mvpstrbad.model.Picture

class PictureViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPictureViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPictureViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picture = intent.parcelable<Picture>("picture")!!
        binding.pictureViewImage.load(picture.pictureUrl)
    }
}