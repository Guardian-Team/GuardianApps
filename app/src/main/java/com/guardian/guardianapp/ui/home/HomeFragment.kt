package com.guardian.guardianapp.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentHomeBinding
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class HomeFragment : Fragment() {
  private var fileName: String = ""

  // Requesting permission to RECORD_AUDIO
  private var permissionToRecordAccepted = false
  private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

  private var player: MediaPlayer? = null
  private var recorder: MediaRecorder? = null

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private var isPressed: Boolean = false
  private var isPlaying: Boolean = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    _binding = FragmentHomeBinding.inflate(inflater, container, false)

    fileName = "${activity?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

    activity?.let {
      ActivityCompat.requestPermissions(
        it,
        permissions,
        REQUEST_RECORD_AUDIO_PERMISSION
      )
    }

    homeViewModel.text.observe(viewLifecycleOwner) {
      binding.textHome.text = it
    }

    binding.btnRecord.setOnClickListener {
      isPressed = !isPressed // reverse
      onRecord(isPressed)
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

    // button play recording
    binding.btnPlay.setOnClickListener {
      isPlaying = !isPlaying
      if(isPlaying){
        onPlay(true)
        binding.btnPlay.setText("Stop Playing")
      } else{
        onPlay(false)
        binding.btnPlay.setText("Play")
      }
    }

    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
      grantResults[0] == PackageManager.PERMISSION_GRANTED
    } else {
      false
    }
    if (!permissionToRecordAccepted) Log.e("Record Permission : ","Permission to Record Not Accepted")
  }

  private fun onRecord(start: Boolean) = if (start) {
    startRecording()
  } else {
    stopRecording()
  }

  private fun onPlay(start: Boolean) = if (start) {
    startPlaying()
  } else {
    stopPlaying()
  }

  private fun startPlaying() {
    player = MediaPlayer().apply {
      try {
        setDataSource(fileName)
        prepare()
        start()
      } catch (e: IOException) {
        Log.e(LOG_TAG, "prepare() failed")
      }
    }
  }

  private fun stopPlaying() {
    player?.release()
    player = null
  }

  private fun startRecording() {
    recorder = MediaRecorder().apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
      setOutputFile(fileName)
      setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

      try {
        prepare()
      } catch (e: IOException) {
        Log.e(LOG_TAG, "prepare() failed")
      }
        start()
    }
  }

  private fun stopRecording() {
    recorder?.apply {
      try{
        stop()
        release()
      } catch (e: IOException) {
        Log.e(LOG_TAG, "release stop() failed")
      }
    }
    recorder = null
  }

  override fun onStop() {
    super.onStop()
    recorder?.release()
    recorder = null
    player?.release()
    player = null
  }

}