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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.fragmentistrbad.databinding.FragmentListBinding
import hr.tvz.android.fragmentistrbad.model.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment(), ItemClick {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var itemClick: ItemClick? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.IO) {

            val db = Room.databaseBuilder(
                requireActivity().applicationContext,
                AppDatabase::class.java, "picture-database"
            ).createFromAsset("database/picture.db").build()
            val pictureDao = db.pictureDao()
            val pictures = pictureDao.getAll()

            lifecycleScope.launch {
                binding.listRecyclerView.apply {
                    adapter = CustomListAdapter(pictures, this@ListFragment)
                    layoutManager = LinearLayoutManager(this@ListFragment.requireActivity())
                }
            }
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

    override fun onPictureClick(picture: Picture) {
        itemClick?.onPictureClick(picture)
    }

}