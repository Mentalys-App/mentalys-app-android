package com.mentalys.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.MainRepository
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    // Themes
    fun getThemeSetting() = preferences.getThemeSetting().asLiveData()
    fun saveThemeSetting(isDark: Boolean) {
        viewModelScope.launch { preferences.saveThemeSetting(isDark) }
    }

    // Notifications
    fun getNotificationSetting() = preferences.getNotificationSetting().asLiveData()
    fun saveNotificationSetting(isEnabled: Boolean) {
        viewModelScope.launch { preferences.saveNotificationSetting(isEnabled) }
    }

}