package hr.tvz.android.mvpstrbad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import hr.tvz.android.mvpstrbad.databinding.ActivityDetailsBinding
import hr.tvz.android.mvpstrbad.extensions.parcelable
import hr.tvz.android.mvpstrbad.model.Picture

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