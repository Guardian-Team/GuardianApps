package com.guardian.guardianapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
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

  fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
      matrix.postRotate(90f)
      Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
      )
    } else {
      matrix.postRotate(-90f)
      matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
      Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
      )
    }
  }
}