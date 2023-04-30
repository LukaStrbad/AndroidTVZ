package hr.tvz.android.listastrbad

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import hr.tvz.android.listastrbad.databinding.ActivityDetailsBinding
import hr.tvz.android.listastrbad.extensions.parcelable
import hr.tvz.android.listastrbad.model.Picture

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picture = intent.parcelable<Picture>("picture")!!

        binding.run {
            pictureDetailsTitle.text = picture.title
            pictureDetailsDescription.text = picture.description

            pictureDetailsImage.setImageResource(picture.pictureResource)
            pictureDetailsImage.setOnClickListener {
                val intent = Intent(this@DetailsActivity, PictureViewActivity::class.java)
                intent.putExtra("picture", picture)
                startActivity(intent)
            }

            pictureDetailsLinkButton.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, picture.webLink.toUri())
                startActivity(browserIntent)
            }
        }
    }
}