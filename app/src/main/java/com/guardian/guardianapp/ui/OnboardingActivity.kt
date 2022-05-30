package com.guardian.guardianapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityOnboardingBinding
import com.guardian.guardianapp.ui.authentication.AuthActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnGetStarted.setOnClickListener {
                startActivity(Intent(this@OnboardingActivity, AuthActivity::class.java))
                finishAffinity()
            }
        }
    }
}