package com.guardian.guardianapp.ui.authentication.signin

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.guardianapp.model.UserModel
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.source.remote.api.ApiConfig
import com.guardian.guardianapp.source.remote.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(private val pref: UserPreference): ViewModel() {
  private val resultResponse = MutableLiveData<Boolean>()
  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun postLogin(email: String, password: String) {
    _isLoading.value = true
    val client = ApiConfig.getAuthApi.postLogin(email, password)
    client.enqueue(object : Callback<LoginResponse> {
      override fun onResponse(
        call: Call<LoginResponse>,
        response: Response<LoginResponse>
      ) {
        _isLoading.value = false
        resultResponse.value = response.isSuccessful

        if (response.isSuccessful){
          val responseBody = response.body()
          if (response.body() != null){
            val model = UserModel(
              responseBody!!.data.name,
              email,
              responseBody.data.id,
              responseBody.data.token,
              true
            )

            saveUser(model)
          }
        }
      }

      override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        _isLoading.value = false
        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
      }
    })
  }

  fun getResultResponse(): LiveData<Boolean> {
    return resultResponse
  }

  fun saveUser(user: UserModel) {
    viewModelScope.launch {
      pref.saveUser(user)
    }
  }
}