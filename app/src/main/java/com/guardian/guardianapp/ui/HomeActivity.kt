package com.guardian.guardianapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.guardian.guardianapp.MainViewModel
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityHomeBinding
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.ui.authentication.AuthActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class HomeActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityHomeBinding
  private lateinit var viewModel: MainViewModel


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupToolbar()

    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.nav_home, R.id.nav_settings, R.id.nav_history
      ), binding.drawerLayout
    )
    setViewModel()
    setupNavigation()

  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_home)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  private fun setupToolbar(){
    setSupportActionBar(binding.appBarHome.toolbar)
    binding.appBarHome.toolbar.setTitleTextAppearance(this, R.style.HeaderTextView)
    binding.appBarHome.toolbar.setNavigationIcon(R.drawable.ic_drawer)
  }

  private fun setupNavigation(){
    val navController = findNavController(R.id.nav_host_fragment_content_home)
    setupActionBarWithNavController(navController, appBarConfiguration)
    binding.navView.setupWithNavController(navController)



    val headerView = binding.navView.getHeaderView(0)
    val navUsername : TextView = headerView.findViewById(R.id.username)
//    val imgProfil : ImageView = headerView.findViewById(R.id.user_picture)
    val btnClose : ImageView = headerView.findViewById(R.id.btn_close)

    viewModel.getUser().observe(this){
      navUsername.text = it.name
    }

    btnClose.setOnClickListener{
      binding.drawerLayout.closeDrawers()
    }
  }

  private fun setViewModel(){
    viewModel = ViewModelProvider(
      this,
      ViewModelFactory(UserPreference.getInstance(dataStore))
    )[MainViewModel::class.java]

    binding.btnSignOut.setOnClickListener {
      viewModel.logout()
      startActivity(Intent(this, AuthActivity::class.java))
      finishAffinity()
    }
  }
}