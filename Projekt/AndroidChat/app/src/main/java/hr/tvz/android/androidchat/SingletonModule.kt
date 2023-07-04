package hr.tvz.android.androidchat

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.tvz.android.androidchat.api.AuthService
import hr.tvz.android.androidchat.api.GroupService
import hr.tvz.android.androidchat.api.MessagesService
import hr.tvz.android.androidchat.api.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context) = TokenManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager) = AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager) = AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit.Builder): AuthService =
        retrofit
            .build()
            .create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideGroupService(retrofit: Retrofit.Builder): GroupService =
        retrofit
            .build()
            .create(GroupService::class.java)

    @Singleton
    @Provides
    fun provideMessagesService(retrofit: Builder): MessagesService =
        retrofit
            .build()
            .create(MessagesService::class.java)
}