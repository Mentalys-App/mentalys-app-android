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
    private val phoneNumberKey = stringPreferencesKey("phone_number_pref")

    private val isHaveProfileKey = booleanPreferencesKey("isHaveProfile_pref")

    // Example usage
    fun getThemeSetting(): Flow<Boolean> = getPreference(themeKey, false)
    suspend fun saveThemeSetting(data: Boolean) = savePreference(themeKey, data)
    suspend fun deleteThemeSetting() = deletePreference(themeKey)

    fun getNotificationSetting(): Flow<Boolean> = getPreference(notificationKey, false)
    suspend fun saveNotificationSetting(data: Boolean) = savePreference(notificationKey, data)
    suspend fun deleteNotificationSetting() = deletePreference(notificationKey)

    fun getLanguageSetting(): Flow<String> = getPreference(languageKey, "English")
    suspend fun saveLanguageSetting(data: String) = savePreference(languageKey, data)
    suspend fun deleteLanguageSetting() = deletePreference(languageKey)

    fun getIsLoginSetting(): Flow<Boolean> = getPreference(isLoginKey, false)
    suspend fun saveIsLoginSetting(data: Boolean) = savePreference(isLoginKey, data)
    suspend fun deleteIsLoginSetting() = deletePreference(isLoginKey)

    fun getUidSetting(): Flow<String> = getPreference(uidKey, "")
    suspend fun saveUidSetting(data: String) = savePreference(uidKey, data)
    suspend fun deleteUidSetting() = deletePreference(uidKey)

    fun getTokenSetting(): Flow<String> = getPreference(tokenKey, "")
    suspend fun saveTokenSetting(data: String) = savePreference(tokenKey, data)
    suspend fun deleteTokenSetting() = deletePreference(tokenKey)

    fun getEmailSetting(): Flow<String> = getPreference(emailKey, "")
    suspend fun saveEmailSetting(data: String) = savePreference(emailKey, data)
    suspend fun deleteEmailSetting() = deletePreference(emailKey)

    fun getUpdatedAtSetting(): Flow<String> = getPreference(updatedAtKey, "")
    suspend fun saveUpdatedAtSetting(data: String) = savePreference(updatedAtKey, data)
    suspend fun deleteUpdatedAtSetting() = deletePreference(updatedAtKey)

    fun getCreatedAtSetting(): Flow<String> = getPreference(createdAtKey, "")
    suspend fun saveCreatedAtSetting(data: String) = savePreference(createdAtKey, data)
    suspend fun deleteCreatedAtSetting() = deletePreference(createdAtKey)

    fun getBirthDateSetting(): Flow<String> = getPreference(birthDateKey, "")
    suspend fun saveBirthDateSetting(data: String) = savePreference(birthDateKey, data)
    suspend fun deleteBirthDateSetting() = deletePreference(birthDateKey)

    fun getGenderSetting(): Flow<String> = getPreference(genderKey, "")
    suspend fun saveGenderSetting(data: String) = savePreference(genderKey, data)
    suspend fun deleteGenderSetting() = deletePreference(genderKey)

    fun getUsernameSetting(): Flow<String> = getPreference(usernameKey, "")
    suspend fun saveUsernameSetting(data: String) = savePreference(usernameKey, data)
    suspend fun deleteUsernameSetting() = deletePreference(usernameKey)

    fun getPhoneNumberSetting(): Flow<String> = getPreference(phoneNumberKey, "")
    suspend fun savePhoneNumberSetting(data: String) = savePreference(phoneNumberKey, data)
    suspend fun deletePhoneNumberSetting() = deletePreference(phoneNumberKey)

    fun getProfilePicSetting(): Flow<String> = getPreference(profilePicKey, "")
    suspend fun saveProfilePicSetting(data: String) = savePreference(profilePicKey, data)
    suspend fun deleteProfilePicSetting() = deletePreference(profilePicKey)

    fun getLocationSetting(): Flow<String> = getPreference(locationKey, "")
    suspend fun saveLocationSetting(data: String) = savePreference(locationKey, data)
    suspend fun deleteLocationSetting() = deletePreference(locationKey)

    fun getFullNameSetting(): Flow<String> = getPreference(fullNameKey, "")
    suspend fun saveFullNameSetting(data: String) = savePreference(fullNameKey, data)
    suspend fun deleteFullNameSetting() = deletePreference(fullNameKey)

    fun getIsHaveProfileSetting(): Flow<Boolean> = getPreference(isHaveProfileKey, false)
    suspend fun saveIsHaveProfileSetting(data: Boolean) = savePreference(isHaveProfileKey, data)
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