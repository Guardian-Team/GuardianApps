package com.guardian.guardianapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private var isPressed: Boolean = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    _binding = FragmentHomeBinding.inflate(inflater, container, false)

    homeViewModel.text.observe(viewLifecycleOwner) {
      binding.textHome.text = it
    }

    binding.btnRecord.setOnClickListener {
      isPressed = !isPressed // reverse
      if (isPressed) {
        binding.btnRecord.setImageResource(R.drawable.ic_press_button_on)
        binding.tvPressToRecord.visibility = View.GONE
        binding.tvRecording.visibility = View.VISIBLE
      } else {
        binding.btnRecord.setImageResource(R.drawable.ic_press_button_off)
        binding.tvPressToRecord.visibility = View.VISIBLE
        binding.tvRecording.visibility = View.GONE
      }
    }

    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}