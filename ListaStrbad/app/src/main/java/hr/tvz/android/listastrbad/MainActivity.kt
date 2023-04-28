package hr.tvz.android.listastrbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.listastrbad.databinding.ActivityMainBinding
import hr.tvz.android.listastrbad.model.Picture
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.BufferedInputStream
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listDataJson = resources.openRawResource(R.raw.list_data)
        val pictures = Json.decodeFromStream<List<Picture>>(listDataJson)

        binding.listRecyclerView.apply {
            adapter = CustomListAdapter(pictures)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}