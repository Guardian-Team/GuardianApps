package com.guardian.guardianapp.ui.authentication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityAuthBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class AuthActivity : AppCompatActivity() {
  private lateinit var binding: ActivityAuthBinding
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAuthBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //init nav host controller
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
    navController = navHostFragment.navController
    setUpToolbar()
  }

  fun setUpToolbarVisibility(isSignUp: Boolean) {
    if (isSignUp) {
      binding.authToolbar.visibility = View.VISIBLE
    } else {
      binding.authToolbar.visibility = View.GONE
    }
  }

  private fun setUpToolbar() {
    setSupportActionBar(binding.authToolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = ""
  }

  fun getDataStore(): DataStore<Preferences>{
    return dataStore
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

}