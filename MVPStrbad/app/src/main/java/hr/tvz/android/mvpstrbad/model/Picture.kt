package hr.tvz.android.mvpstrbad.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Picture(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String,
    @SerialName("image") val image: String,
    @SerialName("web_link") val webLink: String
) : Parcelable
