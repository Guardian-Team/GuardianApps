package com.guardian.guardianapp.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.guardian.guardianapp.ml.ModelAudio
//import com.guardian.guardianapp.ml.ModelZupa
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.*
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

object Helper {
  private const val FILENAME_FORMAT = "dd-MMM-yyyy"

  private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
  ).format(System.currentTimeMillis())

  /**
   * Show toast
   */
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

  /**
   * Rotating bitmap -90 degree
   */
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

  /**
   * Create temporary file
   */
  private fun tempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
  }

  /**
   * Get image from uri
   */
  fun converterUri(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = tempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
  }

  /**
   * Compress Image into 1 Mb
   */
  fun compressImg(file: File): File {
    val imgBitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
      val imgBmpStream = ByteArrayOutputStream()
      imgBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, imgBmpStream)
      val bmpPicByteArray = imgBmpStream.toByteArray()
      streamLength = bmpPicByteArray.size
      compressQuality -= 5
    } while (streamLength > 1000000)
    imgBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
  }

  /**
   * Run MFCC process using python
   */
  fun mfcc(context: Context, path: String): ByteArray {
    if (!Python.isStarted()) {
      Python.start(AndroidPlatform(context))
      Log.i("Python", "python start")
    }
    val python = Python.getInstance()
    val pythonFile = python.getModule("app_preprocess")

    return pythonFile.callAttr("preprocess", path).toJava(ByteArray::class.java)
  }


  /**
   * Run Train using Model
   */
  fun audioDetection(context: Context, buffer: ByteBuffer): FloatArray {
    val model = ModelAudio.newInstance(context)

    // Creates inputs for reference.
    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 44, 13, 1), DataType.FLOAT32)
    inputFeature0.loadBuffer(buffer)

    // Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

    // Releases model resources if no longer used.
    model.close()
    return outputFeature0
  }

//  fun audioDetectionZupa(context: Context, buffer: ByteBuffer): FloatArray {
//    val model = ModelZupa.newInstance(context)
//
//// Creates inputs for reference.
//    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 44, 13, 1), DataType.FLOAT32)
//    inputFeature0.loadBuffer(buffer)
//
//// Runs model inference and gets result.
//    val outputs = model.process(inputFeature0)
//    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
//
//// Releases model resources if no longer used.
//    model.close()
//
//    return outputFeature0
//  }
}