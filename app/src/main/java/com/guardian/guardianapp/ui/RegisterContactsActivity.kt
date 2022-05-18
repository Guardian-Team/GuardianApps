package com.guardian.guardianapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityRegisterContactsBinding

class RegisterContactsActivity : AppCompatActivity() {
  private lateinit var binding : ActivityRegisterContactsBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterContactsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupToolbar()
  }

  private fun setupToolbar(){
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