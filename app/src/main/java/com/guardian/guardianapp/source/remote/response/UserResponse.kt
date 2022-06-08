package com.guardian.guardianapp.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("data")
    val data: ItemUser,

    @field:SerializedName("message")
    val message: String
)

data class ItemUser(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar")
    val avatar: Any,

    @field:SerializedName("email")
    val email: String
)
