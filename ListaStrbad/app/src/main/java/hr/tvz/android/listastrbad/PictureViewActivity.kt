package hr.tvz.android.listastrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.tvz.android.listastrbad.databinding.ActivityPictureViewBinding
import hr.tvz.android.listastrbad.extensions.parcelable
import hr.tvz.android.listastrbad.model.Picture

class PictureViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPictureViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPictureViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picture = intent.parcelable<Picture>("picture")!!
        binding.pictureViewImage.setImageResource(picture.pictureResource)
    }
}