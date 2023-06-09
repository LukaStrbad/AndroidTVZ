package hr.tvz.android.listastrbad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture(
    val title: String,
    val description: String,
    val pictureResource: Int,
    val webLink: String
) : Parcelable
