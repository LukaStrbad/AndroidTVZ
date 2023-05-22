package hr.tvz.android.fragmentistrbad

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import hr.tvz.android.fragmentistrbad.databinding.ActivityMainBinding
import hr.tvz.android.fragmentistrbad.model.Picture

class MainActivity : AppCompatActivity(), ItemClick {
    private lateinit var binding: ActivityMainBinding
    private var twoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("some-tag", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("some-tag", token)
        }

        FirebaseNotificationService.onMessageReceived = { message, context ->
            onNotificationReceived(message, context)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ListFragment>(R.id.main_activity_list_fragment_container_view)
            }
        }

        if (binding.mainActivityListFragmentDetailsContainerView != null) {
            twoPane = true
        }
    }

    private fun onNotificationReceived(message: RemoteMessage, context: Context) = runOnUiThread {
        val intent = Intent(context, NotificationViewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        Toast.makeText(
            this@MainActivity,
            "Notification received: ${message.notification?.title}: ${message.notification?.body}",
            Toast.LENGTH_LONG
        ).show()
        intent.putExtra("title", message.notification?.title)
        intent.putExtra("content", message.notification?.body)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                message.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
            .setSmallIcon(R.drawable.planet)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            var granted = false
            val pushNotificationPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { g ->
                    granted = g
                }
            if (!granted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return@runOnUiThread
            }
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onPictureClick(picture: Picture) {
        if (twoPane) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                replace<ListDetailFragment>(
                    R.id.main_activity_list_fragment_details_container_view,
                    args = Bundle().apply {
                        putParcelable("picture", picture)
                    })
            }
        } else {
            // Start DetailsActivity
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("picture", picture)
            startActivity(intent)
        }
    }

    companion object {
        private const val CHANNEL_ID = "hr.tvz.android.fragmentistrbad.firebase_notification"
    }
}