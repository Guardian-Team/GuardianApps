package com.guardian.guardianapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guardian.guardianapp.ui.authentication.AuthActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startActivity(Intent(this, AuthActivity::class.java))
    finishAffinity()
  }
}