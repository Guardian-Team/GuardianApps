package com.guardian.guardianapp.ui.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.guardianapp.model.UserModel
import com.guardian.guardianapp.model.UserPreference
import kotlinx.coroutines.launch

class SignInViewModel(private val pref: UserPreference): ViewModel() {

  fun saveUser(user: UserModel) {
    viewModelScope.launch {
      pref.saveUser(user)
    }
  }
}