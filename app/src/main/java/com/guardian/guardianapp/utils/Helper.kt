package com.guardian.guardianapp.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

object Helper {
  fun showToastShort(context: Context, text: String) {
    Toast.makeText(
      context,
      text,
      Toast.LENGTH_SHORT
    ).show()
  }

  fun showToastLong(context: FragmentActivity?, text: String) {
    Toast.makeText(
      context,
      text,
      Toast.LENGTH_LONG
    ).show()
  }

}