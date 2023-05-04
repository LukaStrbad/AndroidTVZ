package hr.tvz.android.fragmentistrbad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import hr.tvz.android.fragmentistrbad.databinding.ActivityMainBinding
import hr.tvz.android.fragmentistrbad.model.Picture

class MainActivity : AppCompatActivity(), ItemClick {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ListFragment>(R.id.main_activity_list_fragment_container_view)
            }
        }
    }

    override fun onPictureClick(picture: Picture) {
        // Start DetailsActivity
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("picture", picture)
        startActivity(intent)
    }
}