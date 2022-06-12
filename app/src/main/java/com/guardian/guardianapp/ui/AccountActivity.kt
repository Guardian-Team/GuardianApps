package com.guardian.guardianapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.guardian.guardianapp.MainViewModel
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityAccountBinding
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.utils.Helper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var viewModel: MainViewModel
    private var imgProfile: File? = null

    //Request Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Helper.showToastShort(this, getString(R.string.not_granted))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupToolbar()
        setViewModel()
        setUpListener()
        showLoading()
    }

    private fun setUpListener() {
        binding.apply {
            btnChangePicture.setOnClickListener {
                openGallery()
            }
            btnSave.setOnClickListener {

                if (imgProfile != null) {
                    val file = Helper.compressImg(imgProfile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "avatar",
                        file.name,
                        requestImageFile
                    )
                    val name: RequestBody = binding.inputTextName.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val phone: RequestBody = binding.inputTextPhone.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    viewModel.getUser().observe(this@AccountActivity) {
                        viewModel.updateUserDetailWithAva(
                            it.userid,
                            it.token,
                            name,
                            phone,
                            imageMultipart
                        )
                    }
                } else {
                    viewModel.getUser().observe(this@AccountActivity) {
                        viewModel.updateUserDetail(
                            it.userid,
                            it.token,
                            binding.inputTextName.text.toString(),
                            binding.inputTextPhone.text.toString()
                        )
                    }
                }
                viewModel.getResultResponse().observe(this@AccountActivity) {
                    if (it) {
                        Helper.showToastShort(
                            this@AccountActivity,
                            "Data has been updated!"
                        )
                    } else {
                        Helper.showToastShort(
                            this@AccountActivity,
                            "Data failed to update!"
                        )
                    }
                }
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
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
        return true
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        viewModel.getUser().observe(this) {
            viewModel.getUserDetail(it.userid, it.token)
            viewModel.itemUser.observe(this) {
                binding.apply {
                    inputTextName.setText(it.name)
                    inputTextEmail.setText(it.email)
                    inputTextPhone.setText(it.phone)
                    if (it.avatar != null) {
                        Glide.with(imgProfil)
                            .load(it.avatar)
                            .centerCrop()
                            .into(imgProfil)
                    } else {
                        imgProfil.setImageResource(R.drawable.avatar_null)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherGalerry = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = Helper.converterUri(selectedImg, this)
            imgProfile = myFile
            binding.imgProfil.setImageURI(selectedImg)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherGalerry.launch(chooser)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}