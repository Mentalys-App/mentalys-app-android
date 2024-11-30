package com.mentalys.app.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    // Generic method to retrieve a preference value
    private fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    // Generic method to save a preference value
    private suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Generic method to delete a preference
    private suspend fun <T> deletePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    // Define preference keys
    private val isLoginKey = booleanPreferencesKey("isLogin_settings")
    private val uidKey = stringPreferencesKey("uid_settings")
    private val tokenKey = stringPreferencesKey("token_settings")
    private val emailKey = stringPreferencesKey("email_settings")
    private val themeKey = booleanPreferencesKey("theme_setting")
    private val notificationKey = booleanPreferencesKey("notification_setting")
    private val languageKey = stringPreferencesKey("language_key")

    private val updatedAtKey = stringPreferencesKey("updated_at_pref")
    private val createdAtKey = stringPreferencesKey("created_at_pref")
    private val birthDateKey = stringPreferencesKey("birth_date_pref")
    private val genderKey = stringPreferencesKey("gender_pref")
    private val usernameKey = stringPreferencesKey("username_pref")
    private val profilePicKey = stringPreferencesKey("profile_pic_pref")
    private val locationKey = stringPreferencesKey("location_pref")
    private val fullNameKey = stringPreferencesKey("full_name_pref")

    private val isHaveProfileKey = booleanPreferencesKey("isHaveProfile_pref")

    // Example usage
    fun getThemeSetting(): Flow<Boolean> = getPreference(themeKey, false)
    suspend fun saveThemeSetting(isDarkMode: Boolean) = savePreference(themeKey, isDarkMode)
    suspend fun deleteThemeSetting() = deletePreference(themeKey)

    fun getNotificationSetting(): Flow<Boolean> = getPreference(notificationKey, false)
    suspend fun saveNotificationSetting(isEnabled: Boolean) = savePreference(notificationKey, isEnabled)
    suspend fun deleteNotificationSetting() = deletePreference(notificationKey)

    fun getLanguageSetting(): Flow<String> = getPreference(languageKey, "English")
    suspend fun saveLanguageSetting(language: String) = savePreference(languageKey, language)
    suspend fun deleteLanguageSetting() = deletePreference(languageKey)

    fun getIsLoginSetting(): Flow<Boolean> = getPreference(isLoginKey, false)
    suspend fun saveIsLoginSetting(isLogin: Boolean) = savePreference(isLoginKey, isLogin)
    suspend fun deleteIsLoginSetting() = deletePreference(isLoginKey)

    fun getUidSetting(): Flow<String> = getPreference(uidKey, "")
    suspend fun saveUidSetting(uid: String) = savePreference(uidKey, uid)
    suspend fun deleteUidSetting() = deletePreference(uidKey)

    fun getTokenSetting(): Flow<String> = getPreference(tokenKey, "")
    suspend fun saveTokenSetting(token: String) = savePreference(tokenKey, token)
    suspend fun deleteTokenSetting() = deletePreference(tokenKey)

    fun getEmailSetting(): Flow<String> = getPreference(emailKey, "")
    suspend fun saveEmailSetting(email: String) = savePreference(emailKey, email)
    suspend fun deleteEmailSetting() = deletePreference(emailKey)

    fun getUpdatedAtSetting(): Flow<String> = getPreference(updatedAtKey, "")
    suspend fun saveUpdatedAtSetting(updatedAt: String) = savePreference(updatedAtKey, updatedAt)
    suspend fun deleteUpdatedAtSetting() = deletePreference(updatedAtKey)

    fun getCreatedAtSetting(): Flow<String> = getPreference(createdAtKey, "")
    suspend fun saveCreatedAtSetting(createdAt: String) = savePreference(createdAtKey, createdAt)
    suspend fun deleteCreatedAtSetting() = deletePreference(createdAtKey)

    fun getBirthDateSetting(): Flow<String> = getPreference(birthDateKey, "")
    suspend fun saveBirthDateSetting(birthDate: String) = savePreference(birthDateKey, birthDate)
    suspend fun deleteBirthDateSetting() = deletePreference(birthDateKey)

    fun getGenderSetting(): Flow<String> = getPreference(genderKey, "")
    suspend fun saveGenderSetting(gender: String) = savePreference(genderKey, gender)
    suspend fun deleteGenderSetting() = deletePreference(genderKey)

    fun getUsernameSetting(): Flow<String> = getPreference(usernameKey, "")
    suspend fun saveUsernameSetting(username: String) = savePreference(usernameKey, username)
    suspend fun deleteUsernameSetting() = deletePreference(usernameKey)

    fun getProfilePicSetting(): Flow<String> = getPreference(profilePicKey, "")
    suspend fun saveProfilePicSetting(profilePic: String) = savePreference(profilePicKey, profilePic)
    suspend fun deleteProfilePicSetting() = deletePreference(profilePicKey)

    fun getLocationSetting(): Flow<String> = getPreference(locationKey, "")
    suspend fun saveLocationSetting(location: String) = savePreference(locationKey, location)
    suspend fun deleteLocationSetting() = deletePreference(locationKey)

    fun getFullNameSetting(): Flow<String> = getPreference(fullNameKey, "")
    suspend fun saveFullNameSetting(location: String) = savePreference(fullNameKey, location)
    suspend fun deleteFullNameSetting() = deletePreference(fullNameKey)

    fun getIsHaveProfileSetting(): Flow<Boolean> = getPreference(isHaveProfileKey, false)
    suspend fun saveIsHaveProfileSetting(isProfile: Boolean) = savePreference(isHaveProfileKey, isProfile)
    suspend fun deleteIsHaveProfileSetting() = deletePreference(isHaveProfileKey)

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