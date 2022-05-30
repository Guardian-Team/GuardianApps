package com.guardian.guardianapp.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityHomeBinding

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
    val btnClose : ImageView = headerView.findViewById(R.id.btn_close)

    btnClose.setOnClickListener{
      binding.drawerLayout.closeDrawers()
    }

    navUsername.text = "Si Fulan"
  }
}