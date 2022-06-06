package com.guardian.guardianapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
  val name: String,
  val email: String,
  val userid: Int,
  val token: String,
  val islogin: Boolean
): Parcelable