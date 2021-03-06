package com.guardian.guardianapp.source.remote.api

import com.guardian.guardianapp.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("/api/user/{id}")
    fun getUserDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<UserResponse>


    @FormUrlEncoded
    @PUT("/api/user/{id}")
    fun updateUserDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String
    ): Call<UserResponse>

    @Multipart
    @PUT("/api/user/{id}")
    fun updateUserDetailWithAva(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<UserResponse>

    @GET("/api/contact/{id}")
    fun getUserContact(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<RegisterContactResponse>

    @DELETE("/api/contact/{id}/{contactId}")
    fun delUserContact(
        @Path("id") id: Int,
        @Path("contactId") contactId: Int,
        @Header("Authorization") token: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/api/contact/{id}")
    fun addUserContact(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String
    ): Call<AddContactResponse>
}