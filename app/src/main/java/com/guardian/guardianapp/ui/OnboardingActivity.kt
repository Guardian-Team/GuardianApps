package com.guardian.guardianapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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