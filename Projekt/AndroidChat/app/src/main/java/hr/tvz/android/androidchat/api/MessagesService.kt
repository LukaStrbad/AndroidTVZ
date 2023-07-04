package hr.tvz.android.androidchat.api

import hr.tvz.android.androidchat.model.FormMessage
import hr.tvz.android.androidchat.model.Message
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MessagesService {
    @PUT("Messages/send")
    suspend fun send(@Body formMessage: FormMessage): Response<ResponseBody>

    @GET("Messages/lastMessageTime")
    suspend fun lastMessageTime(): Response<String>

    @GET("Messages/lastMessageTimeInGroup")
    suspend fun lastMessageTimeInGroup(@Query("groupId") groupId: Int): Response<String>

    @GET("Messages/lastMessage")
    suspend fun lastMessage(@Query("groupId") groupId: Int): Response<Message>

    @GET("Messages/allMessages")
    suspend fun getAllMessages(@Query("groupId") groupId: Int): Response<List<Message>>
}