package hr.tvz.android.fragmentistrbad

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.fragmentistrbad.databinding.FragmentListBinding
import hr.tvz.android.fragmentistrbad.model.Picture

class ListFragment : Fragment(), ItemClick {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var itemClick: ItemClick? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pictures = getPictures()

        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.listRecyclerView.apply {
            adapter = CustomListAdapter(List(100) { pictures }.flatten(), this@ListFragment)
            layoutManager = LinearLayoutManager(this@ListFragment.requireActivity())
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

        ContextCompat.registerReceiver(requireContext(), br, filter, receiverFlags)


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        check(context is ItemClick) { "Activity must implement ItemClick interface" }
        itemClick = context
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

    override fun onPictureClick(picture: Picture) {
        itemClick?.onPictureClick(picture)
    }

}