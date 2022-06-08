package com.guardian.guardianapp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.guardian.guardianapp.model.UserModel
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.source.remote.api.ApiConfig
import com.guardian.guardianapp.source.remote.response.ItemUser
import com.guardian.guardianapp.source.remote.response.UserResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference): ViewModel() {
    private val _itemUser = MutableLiveData<ItemUser>()
    val itemUser: LiveData<ItemUser> = _itemUser
    private val resultResponse = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetail(id: Int, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.getUserDetail(id, "Bearer $token")
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _itemUser.value = responseBody.data
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updateUserDetailWithAva(id: Int, token: String, name: RequestBody, phone: RequestBody, avatar: MultipartBody.Part){
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.updateUserDetailWithAva(id, "Bearer $token", name, phone, avatar)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _itemUser.value = responseBody.data
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updateUserDetail(id: Int, token: String, name: String, phone: String){
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.updateUserDetail(id, "Bearer $token", name, phone)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _itemUser.value = responseBody.data
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getResultResponse(): LiveData<Boolean> {
        return resultResponse
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}