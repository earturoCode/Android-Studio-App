package com.example.gamesapplication.dataStore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
class UserPreferencesManager private constructor(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    companion object {
        private var INSTANCE: UserPreferencesManager? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = UserPreferencesManager(context)
            }
        }

        fun get(): UserPreferencesManager {
            return INSTANCE ?: throw IllegalStateException("UserPreferencesManager not initialized")
        }
    }
    object UserPreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val TOKEN = stringPreferencesKey("token")
    }
    suspend fun saveUserData(userId: String, token: String) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = userId
            prefs[UserPreferencesKeys.TOKEN] = token
        }
    }

    val userData: Flow<Pair<String?, String?>> = dataStore.data.map { prefs ->
        prefs[UserPreferencesKeys.USER_ID] to prefs[UserPreferencesKeys.TOKEN]
    }
}
val Context.dataStore by preferencesDataStore(name = "user_prefs")


