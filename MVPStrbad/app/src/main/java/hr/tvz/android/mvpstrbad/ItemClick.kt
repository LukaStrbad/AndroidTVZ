package hr.tvz.android.mvpstrbad

import hr.tvz.android.mvpstrbad.model.Picture

interface ItemClick {
    fun onPictureClick(picture: Picture)
}