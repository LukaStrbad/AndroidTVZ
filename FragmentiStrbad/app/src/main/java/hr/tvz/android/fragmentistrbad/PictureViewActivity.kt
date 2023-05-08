package hr.tvz.android.fragmentistrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import hr.tvz.android.fragmentistrbad.databinding.ActivityPictureViewBinding
import hr.tvz.android.fragmentistrbad.extensions.parcelable
import hr.tvz.android.fragmentistrbad.model.Picture

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