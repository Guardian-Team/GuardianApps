package com.guardian.guardianapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.guardian.guardianapp.model.SettingsModel
import com.guardian.guardianapp.model.UserPreference
import kotlinx.coroutines.launch

class ApplicationViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUserSettings(): LiveData<SettingsModel> {
        return pref.getUserSettings().asLiveData()
    }

    fun saveUserSettings(userSettings: SettingsModel) {
        viewModelScope.launch {
            pref.saveUserSettings(userSettings)
        }
    }
}