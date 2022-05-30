package com.guardian.guardianapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.guardian.guardianapp.ui.OnboardingActivity
import com.guardian.guardianapp.ui.authentication.AuthActivity

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startActivity(Intent(this, OnboardingActivity::class.java))
    finishAffinity()
  }
}