package hr.tvz.android.androidchat.api

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY: Preferences.Key<String> = stringPreferencesKey("jwt_token")
        private val USERNAME_KEY: Preferences.Key<String> = stringPreferencesKey("username")
        private val PASSWORD_KEY: Preferences.Key<String> = stringPreferencesKey("password")
    }

    private val Context.dataStore by preferencesDataStore(
        name = "preferences"
    )

    fun getToken() : Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    fun getUsername() : Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit {preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    fun getPassword() : Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PASSWORD_KEY]
        }
    }

    suspend fun savePassword(password: String) {
        context.dataStore.edit {preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }
}