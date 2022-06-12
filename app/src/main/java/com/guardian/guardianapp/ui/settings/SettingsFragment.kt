package com.guardian.guardianapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSettingsBinding
import com.guardian.guardianapp.ui.AccountActivity
import com.guardian.guardianapp.ui.ApplicationActivity
import com.guardian.guardianapp.utils.Helper

class SettingsFragment : Fragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    setupListener()
    return binding.root
  }

  private fun setupListener(){
    binding.apply {
      setAccount.setOnClickListener {
        startActivity(Intent(activity, AccountActivity::class.java))
      }
      setApplication.setOnClickListener {
        startActivity(Intent(activity, ApplicationActivity::class.java))
      }
      setAboutUs.setOnClickListener {
        Helper.showToastLong(activity, getString(R.string.available_soon))
      }
      setHelpSupport.setOnClickListener {
        Helper.showToastLong(activity, getString(R.string.available_soon))
      }
      setLanguage.setOnClickListener {
        Helper.showToastLong(activity, getString(R.string.available_soon))
      }
      setTheme.setOnClickListener {
        Helper.showToastLong(activity, getString(R.string.available_soon))
      }
      toggleDarkMode.setOnClickListener {
        Helper.showToastLong(activity, getString(R.string.available_soon))
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}