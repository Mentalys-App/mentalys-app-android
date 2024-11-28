package com.mentalys.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsPreferencesKeys {
    val NAME = stringPreferencesKey("user_name")
    val EMAIL = stringPreferencesKey("user_email")
}

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val isLoginKey = booleanPreferencesKey("isLogin_settings")
    private val uidKey = stringPreferencesKey("uid_settings")
    private val tokenKey = stringPreferencesKey("token_settings")
    private val nameKey = SettingsPreferencesKeys.NAME
    private val emailKey = SettingsPreferencesKeys.EMAIL
    private val themeKey = booleanPreferencesKey("theme_setting")
    private val notificationKey = booleanPreferencesKey("notification_setting")
    private val languageKey = stringPreferencesKey("language_key")

    // Get theme
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    // Save theme
    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkMode
        }
    }

    // Get notification
    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[notificationKey] ?: false
        }
    }

    // Save notification
    suspend fun saveNotificationSetting(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[notificationKey] = isEnabled
        }
    }

    // Get language
    fun getLanguageSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[languageKey] ?: "English"
        }
    }

    // Save language
    suspend fun saveLanguageSetting(language: String) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    // Get uid
    fun getIsLoginSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isLoginKey] ?: false
        }
    }

    // Save uid
    suspend fun saveIsLoginSetting(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[isLoginKey] = isLogin
        }
    }

    suspend fun deleteIsLoginSetting() {
        dataStore.edit { preferences ->
            preferences.remove(isLoginKey)
        }
    }

    // Get uid
    fun getUidSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[uidKey] ?: ""
        }
    }

    // Save uid
    suspend fun saveUidSetting(uid: String) {
        dataStore.edit { preferences ->
            preferences[uidKey] = uid
        }
    }

    suspend fun deleteUidSetting() {
        dataStore.edit { preferences ->
            preferences.remove(uidKey)
        }
    }

    // Get token
    fun getTokenSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    // Save token
    suspend fun saveTokenSetting(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun deleteTokenSetting() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }

    // Get name
    fun getNameSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[nameKey] ?: ""
        }
    }

    // Save name
    suspend fun saveNameSetting(name: String) {
        dataStore.edit { preferences ->
            preferences[nameKey] = name
        }
    }

    // Get email
    fun getEmailSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[emailKey] ?: "empty"
        }
    }

    // Save email
    suspend fun saveEmailSetting(email: String) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    suspend fun deleteEmailSetting() {
        dataStore.edit { preferences ->
            preferences.remove(emailKey)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}