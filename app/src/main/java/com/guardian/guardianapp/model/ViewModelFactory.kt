package com.guardian.guardianapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.ui.authentication.signin.SignInViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
        SignInViewModel(pref) as T
      }
//      modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
//        SignInViewModel(pref) as T
//      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}