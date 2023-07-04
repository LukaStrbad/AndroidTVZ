package hr.tvz.android.androidchat.api

import hr.tvz.android.androidchat.model.Auth
import hr.tvz.android.androidchat.model.AuthRegister
import hr.tvz.android.androidchat.model.AuthResponse
import hr.tvz.android.androidchat.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("User/login")
    suspend fun login(@Body auth: Auth): Response<AuthResponse>

    @POST("User/create")
    suspend fun register(@Body auth: AuthRegister): Response<AuthResponse>

    @GET("User/profile")
    suspend fun getProfile(): Response<User>

    @GET("User/getUsers")
    suspend fun getUsers(@Query("nameContains") nameContains: String = ""): Response<List<User>>
}