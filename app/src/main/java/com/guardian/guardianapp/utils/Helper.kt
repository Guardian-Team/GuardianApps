package com.guardian.guardianapp.utils

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.guardian.guardianapp.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Helper {
  private const val FILENAME_FORMAT = "dd-MMM-yyyy"

  private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
  ).format(System.currentTimeMillis())

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

  fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
      File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
      mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
  }

}