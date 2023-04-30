package hr.tvz.android.listastrbad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.listastrbad.databinding.ActivityMainBinding
import hr.tvz.android.listastrbad.model.Picture
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.BufferedInputStream
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pictures = getPictures()

        binding.listRecyclerView.apply {
            adapter = CustomListAdapter(List(100) { pictures }.flatten())
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val br: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                StringBuilder().apply {
                    append("Action: ${intent.action}\n")
                    append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
                    toString().also { log ->
                        Log.d("BroadcastReceiver", log)

                        val snackbarText = when (intent.action) {
                            Intent.ACTION_POWER_CONNECTED -> getString(R.string.power_connected)
                            Intent.ACTION_POWER_DISCONNECTED -> getString(R.string.power_disconnected)
                            else -> getString(R.string.unknown_action)
                        }

                        Snackbar.make(binding.root, snackbarText, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Plugged in/unplugged intent filter
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }

        ContextCompat.registerReceiver(applicationContext, br, filter, receiverFlags)
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