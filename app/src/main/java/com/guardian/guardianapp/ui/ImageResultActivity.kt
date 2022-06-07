package com.guardian.guardianapp.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityImageResultBinding
import com.guardian.guardianapp.ml.ModelImage1
import com.guardian.guardianapp.utils.Helper
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteOrder

class ImageResultActivity : AppCompatActivity() {
  private lateinit var binding: ActivityImageResultBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityImageResultBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val path = intent.getStringExtra(EXTRA_PATH_IMAGE)!!
    Log.d("path", path)

    machineLearning(path)
  }

  private fun machineLearning(path: String) {
    var bitmap = BitmapFactory.decodeFile(path)
    bitmap = Helper.rotateBitmap(bitmap, true)
    binding.viewBitmap.setImageBitmap(bitmap)

    val model = ModelImage1.newInstance(this)

    val imageProcessor = ImageProcessor.Builder()
      // Resize using Bilinear and Nearest Neighbor methods
      .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.BILINEAR))
      .add(ResizeWithCropOrPadOp(150, 150))
      .build()

    val image = TensorImage(DataType.FLOAT32)
    image.load(bitmap)
    val processedImage = imageProcessor.process(image)

    val byteBuffer = processedImage.buffer
    byteBuffer.order(ByteOrder.nativeOrder())

    // Creates inputs for reference.
    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
    Log.d("shape byteBuffer   : ", byteBuffer.toString())
    Log.d("shape inputFeature : ", inputFeature0.buffer.toString())
    inputFeature0.loadBuffer(byteBuffer)

    // Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

    // Releases model resources if no longer used.
    setupToolbar()
    model.close()

    // show result
    if (outputFeature0[0] < 0.8) {
      binding.tvResult.text = getString(R.string.not_violence)
    } else{
      binding.tvResult.text = getString(R.string.violence)
    }
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    binding.toolbar.setNavigationIcon(R.drawable.ic_back_grey)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    finish()
    return true
  }

  companion object {
    const val EXTRA_PATH_IMAGE = "image"
  }
}