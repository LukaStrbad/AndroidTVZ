package hr.tvz.android.fragmentistrbad

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.fragmentistrbad.databinding.ActivityNotificationViewBinding

class NotificationViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        println(savedInstanceState?.getString("content") ?: "No content")

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        Log.e("NOTIFICATION-TAG", "onCreate: $title, $content")
        binding.activityNotificationsTitle.text = title
        binding.activityNotificationsContent.text = content
    }
}