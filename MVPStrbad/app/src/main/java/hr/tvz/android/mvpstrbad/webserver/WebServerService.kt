package hr.tvz.android.mvpstrbad.webserver

import hr.tvz.android.mvpstrbad.model.Picture
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface WebServerService {
    @GET("Picture/allPictures")
    fun getAllPictures(): Call<ResponseBody>

    @GET("Picture/latestPicture")
    fun getLatestPicture(): Call<ResponseBody>
}