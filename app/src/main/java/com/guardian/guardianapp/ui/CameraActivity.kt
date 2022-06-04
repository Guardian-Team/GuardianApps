package com.guardian.guardianapp.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityCameraBinding
import com.guardian.guardianapp.utils.Helper
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
  private lateinit var binding: ActivityCameraBinding
  private var imageCapture: ImageCapture? = null
  private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Request camera permissions
    if (allPermissionsGranted()) {
      startCamera()
    } else {
      ActivityCompat.requestPermissions(
        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
      )
    }

    //listeners for take photo
    binding.btnImageCapture.setOnClickListener { takePhoto() }
  }

  private fun takePhoto() {
    val imageCapture = imageCapture ?: return

    // Create time stamped name
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
      .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, name)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GuardianApps")
      }
    }
    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions
      .Builder(
        contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
      )
      .build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    imageCapture.takePicture(
      outputOptions,
      ContextCompat.getMainExecutor(this),
      object : ImageCapture.OnImageSavedCallback {
        override fun onError(exc: ImageCaptureException) {
          Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
          Helper.showToastShort(baseContext, exc.message.toString())
        }

        override fun
          onImageSaved(output: ImageCapture.OutputFileResults) {
          val msg = "Photo capture succeeded: ${output.savedUri}"
          Helper.showToastShort(baseContext, msg)
          Log.d(TAG, msg)
        }
      }
    )
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
      val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
      val preview = Preview.Builder()
        .build()
        .also {
          it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }

      imageCapture = ImageCapture.Builder().build()

      try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
          this,
          cameraSelector,
          preview,
          imageCapture
        )
      } catch (exc: Exception) {
        Helper.showToastShort(this,getString(R.string.failed_open_camera))
      }
    }, ContextCompat.getMainExecutor(this))
  }

  private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(
      baseContext, it
    ) == PackageManager.PERMISSION_GRANTED
  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>, grantResults:
    IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_CODE_PERMISSIONS) {
      if (allPermissionsGranted()) {
        startCamera()
      } else {
        Helper.showToastShort(this, getString(R.string.camera_permission_not_granted))
        finish()
      }
    }
  }

  public override fun onResume() {
    super.onResume()
    startCamera()
  }

  companion object {
    private const val TAG = "CameraX"
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private const val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS =
      mutableListOf(
        Manifest.permission.CAMERA
      ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
          add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
      }.toTypedArray()
  }
}