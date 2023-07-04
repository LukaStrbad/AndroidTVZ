package hr.tvz.android.androidchat.api

import hr.tvz.android.androidchat.model.Group
import hr.tvz.android.androidchat.model.GroupForm
import hr.tvz.android.androidchat.model.Message
import hr.tvz.android.androidchat.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface GroupService {
    @POST("Group/create")
    suspend fun createGroup(@Body group: GroupForm): Response<ResponseBody>

    @GET("Group/list")
    suspend fun getGroups(): Response<List<Group>>

    @GET("Group/{groupId}/members")
    suspend fun getMembers(@Path("groupId") groupId: Int): Response<List<User>>

    @POST("Group/{groupId}/addMember")
    suspend fun addMember(
        @Path("groupId") groupId: Int,
        @Body username: String
    ): Response<ResponseBody>

    @GET("Group/{groupId}/amIOwner")
    suspend fun amIOwner(@Path("groupId") groupId: Int): Response<Boolean>

    @POST("Group/{groupId}/removeMember")
    suspend fun removeMember(
        @Path("groupId") groupId: Int,
        @Body username: String
    ): Response<ResponseBody>
}