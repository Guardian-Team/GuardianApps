package com.guardian.guardianapp.ui.authentication.signup

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.guardianapp.source.remote.api.ApiConfig
import com.guardian.guardianapp.source.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val resultResponse = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val messageRes = MutableLiveData<String>()

    fun postRegister(username: String, email: String, password: String, phone: String) {
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.postRegister(username, email, password, phone)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
                messageRes.value = response.body()?.message.toString()
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getResultResponse(): LiveData<Boolean> {
        return resultResponse
    }
    fun getMessageResponse(): LiveData<String>{
        return messageRes
    }
}