package com.guardian.guardianapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.ui.HomeActivity
import com.guardian.guardianapp.ui.OnboardingActivity
import com.guardian.guardianapp.ui.authentication.AuthActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            viewModel.getUser().observe(this) {
                if (it.islogin) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                } else if (!it.islogin && it.isfirstinstall) {
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    finishAffinity()
                } else {
                startActivity(Intent(this, AuthActivity::class.java))
                finishAffinity()
                }
            }
        }, DELAY_TIME)

    }

    companion object {
        const val DELAY_TIME = 500L
    }
}