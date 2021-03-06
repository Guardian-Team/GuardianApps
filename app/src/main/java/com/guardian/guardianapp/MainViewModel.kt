package com.guardian.guardianapp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.guardian.guardianapp.model.SettingsModel
import com.guardian.guardianapp.model.UserModel
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.source.remote.api.ApiConfig
import com.guardian.guardianapp.source.remote.response.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference): ViewModel() {
    private val _itemUser = MutableLiveData<ItemUser>()
    val itemUser: LiveData<ItemUser> = _itemUser
    private val _itemContactUser = MutableLiveData<List<DataItem>>()
    val itemUserContact: LiveData<List<DataItem>> = _itemContactUser
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

    fun getListContactUser(id: Int, token: String){
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.getUserContact(id, "Bearer $token")
        client.enqueue(object : Callback<RegisterContactResponse> {
            override fun onResponse(
                call: Call<RegisterContactResponse>,
                response: Response<RegisterContactResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _itemContactUser.value = responseBody.data
                    }
                }
            }

            override fun onFailure(call: Call<RegisterContactResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun delContactUser(id: Int, contactId: Int, token: String){
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.delUserContact(id, contactId,"Bearer $token")
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addContact(id: Int, token: String, name: String, phone: String){
        _isLoading.value = true
        val client = ApiConfig.getAuthApi.addUserContact(id, token, name, phone)
        client.enqueue(object : Callback<AddContactResponse> {
            override fun onResponse(
                call: Call<AddContactResponse>,
                response: Response<AddContactResponse>
            ) {
                _isLoading.value = false
                resultResponse.value = response.isSuccessful
            }

            override fun onFailure(call: Call<AddContactResponse>, t: Throwable) {
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

    fun getUserSettings(): LiveData<SettingsModel> {
        return pref.getUserSettings().asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}