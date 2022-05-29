package com.guardian.guardianapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
  private lateinit var binding: ActivityAccountBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAccountBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupToolbar()
    setUpListener()
  }

  private fun setUpListener() {
    binding.apply {
      btnChangePicture.setOnClickListener {
        Toast.makeText(this@AccountActivity, "Change Picture Clicked!", Toast.LENGTH_SHORT).show()
      }
      btnSave.setOnClickListener {
        Toast.makeText(this@AccountActivity, "Save Edit Clicked!", Toast.LENGTH_SHORT).show()
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
}