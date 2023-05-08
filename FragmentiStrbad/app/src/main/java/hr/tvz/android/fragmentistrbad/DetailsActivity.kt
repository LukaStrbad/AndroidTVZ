package hr.tvz.android.fragmentistrbad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.add
import androidx.fragment.app.commit
import hr.tvz.android.fragmentistrbad.databinding.ActivityDetailsBinding
import hr.tvz.android.fragmentistrbad.extensions.parcelable
import hr.tvz.android.fragmentistrbad.model.Picture

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val picture = intent.parcelable<Picture>("picture")

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListDetailFragment>(
                R.id.list_details_fragment_container_view,
                args = Bundle().apply { putParcelable("picture", picture) }
            )
        }
    }
}