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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pictures = getPictures()

        binding.listRecyclerView.apply {
            adapter = CustomListAdapter(List(100) { getPictures() }.flatten())
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun getPictures() = listOf(
        Picture(
            getString(R.string.picture10_title),
            getString(R.string.picture10_desc),
            R.drawable.picture_2023_04_10,
            "https://apod.nasa.gov/apod/ap230410.html"
        ),
        Picture(
            getString(R.string.picture11_title),
            getString(R.string.picture11_desc),
            R.drawable.picture_2023_04_11,
            "https://apod.nasa.gov/apod/ap230411.html"
        ),
        Picture(
            getString(R.string.picture12_title),
            getString(R.string.picture12_desc),
            R.drawable.picture_2023_04_12,
            "https://apod.nasa.gov/apod/ap230412.html"
        ),
        Picture(
            getString(R.string.picture13_title),
            getString(R.string.picture13_desc),
            R.drawable.picture_2023_04_13,
            "https://apod.nasa.gov/apod/ap230413.html"
        ),
        Picture(
            getString(R.string.picture14_title),
            getString(R.string.picture14_desc),
            R.drawable.picture_2023_04_14,
            "https://apod.nasa.gov/apod/ap230414.html"
        ),
    )
}