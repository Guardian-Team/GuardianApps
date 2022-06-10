package com.guardian.guardianapp.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddContactResponse(

	@field:SerializedName("data")
	val data: DataContact,

	@field:SerializedName("message")
	val message: String
)

data class DataContact(

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String
)
