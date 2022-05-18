package com.guardian.guardianapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.guardian.guardianapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityHomeBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.appBarHome.toolbar)
    binding.appBarHome.toolbar.setTitleTextAppearance(this, R.style.HeaderTextView)

    val drawerLayout: DrawerLayout = binding.drawerLayout
    val navView: NavigationView = binding.navView
    val navController = findNavController(R.id.nav_host_fragment_content_home)

    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.nav_home, R.id.nav_settings, R.id.nav_history
      ), drawerLayout
    )

    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    binding.appBarHome.toolbar.setNavigationIcon(R.drawable.ic_drawer)

    val navigationView : NavigationView  = findViewById(R.id.nav_view)
    val headerView = navigationView.getHeaderView(0)
    val navUsername : TextView = headerView.findViewById(R.id.username)
    val btnClose : ImageView = headerView.findViewById(R.id.btn_close)

    btnClose.setOnClickListener{
      binding.drawerLayout.closeDrawers()
    }

    navUsername.text = "Si Fulan"

  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_home)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}