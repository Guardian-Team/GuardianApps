package com.guardian.guardianapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityApplicationBinding

class ApplicationActivity : AppCompatActivity() {
  private lateinit var binding : ActivityApplicationBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityApplicationBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupToolbar()
    
    binding.registeredContacts.setOnClickListener{
      startActivity(Intent(this, RegisterContactsActivity::class.java))
    }
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