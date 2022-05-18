package com.guardian.guardianapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.databinding.FragmentSettingsBinding
import com.guardian.guardianapp.ui.ApplicationActivity

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
    binding.setApplication.setOnClickListener {
      startActivity(Intent(activity, ApplicationActivity::class.java))
    }

    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}