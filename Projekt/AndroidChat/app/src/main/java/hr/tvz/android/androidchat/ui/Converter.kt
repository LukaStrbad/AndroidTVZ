package hr.tvz.android.androidchat.ui

import android.widget.EditText
import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

object Converter {
    @JvmStatic
    fun dateToString(
        value: String?
    ): String? {
        if (value == null) {
            return null
        }
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        return format.format(formatter.parse(value))
    }
}