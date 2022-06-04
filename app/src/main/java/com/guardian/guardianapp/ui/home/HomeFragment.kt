package com.guardian.guardianapp.ui.home

import android.Manifest
import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentHomeBinding
import com.guardian.guardianapp.ui.CameraActivity
import com.guardian.guardianapp.utils.Helper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
  private val speech: SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(activity) }
  private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

  private var isActivated: Boolean = false
  private val activationKeyword: String = "test"

  private lateinit var fileName: String
  private var recorder: MediaRecorder? = null
  private var isPressed: Boolean = false

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  // Requesting permission
  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
      if (isGranted) {
        Log.i("DEBUG", "permission granted")
      } else {
        Log.i("DEBUG", "permission denied")
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    _binding = FragmentHomeBinding.inflate(inflater, container, false)

    // request permission to record audio
    requestPermission.launch(Manifest.permission.RECORD_AUDIO)

    setSpeechRecognizerIntent()
    setSpeechRecognizer()
    buttonListener()
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun buttonListener() {
    binding.btnRecord.setOnClickListener {
      isPressed = !isPressed // reverse
      onRecord(isPressed)
      changeRecordButton(isPressed)
    }

    binding.btnHistory.setOnClickListener {
      findNavController().navigate(R.id.action_nav_home_to_nav_history)
    }

    binding.switchStandbyMode.setOnCheckedChangeListener { _, isChecked ->
      val message = if (isChecked) "Standby Mode On" else "StandbyMode Off"
      Helper.showToastShort(requireActivity(), message)

      if (isChecked) {
        speech.startListening(speechRecognizerIntent)
      } else {
        speech.stopListening()
      }
    }

    binding.btnTakePicture.setOnClickListener {
      val intent = Intent(activity, CameraActivity::class.java)
      startActivity(intent)
    }
  }

  private fun changeRecordButton(isPressed: Boolean) {
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

  private fun setFileName() {
    val now = Date()
    val formatter = SimpleDateFormat("dd.MMM.yyyy_hhmmsss", Locale.getDefault())
    fileName =
      "${activity?.getExternalFilesDir("/")?.absolutePath}/Record_${formatter.format(now)}.3gp"
  }

  private fun onRecord(start: Boolean) = if (start) {
    startRecording()
  } else {
    stopRecording()
  }

  private fun startRecording() {
    setFileName()

    binding.chronometer.base = SystemClock.elapsedRealtime()
    binding.chronometer.start()

    recorder = MediaRecorder().apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
      setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
      setOutputFile(fileName)

      try {
        prepare()
      } catch (e: IOException) {
        Log.e(LOG_AUDIO_TAG, "prepare() failed")
      }
      start()
    }
  }

  private fun stopRecording() {
    binding.chronometer.stop()
    recorder?.apply {
      try {
        stop()
        release()
      } catch (e: IOException) {
        Log.e(LOG_AUDIO_TAG, "release stop() failed")
      }
    }
    recorder = null
  }

  private fun setSpeechRecognizerIntent() {
    speechRecognizerIntent.putExtra(
      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    speechRecognizerIntent.putExtra(
      RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
      1000
    )
    speechRecognizerIntent.putExtra(
      RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
      1000
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1500)
  }

  private fun setSpeechRecognizer() {
    speech.setRecognitionListener(object : RecognitionListener {
      override fun onReadyForSpeech(bundle: Bundle) {}

      override fun onBeginningOfSpeech() {}

      override fun onRmsChanged(v: Float) {}

      override fun onBufferReceived(bytes: ByteArray) {}

      override fun onEndOfSpeech() {}

      override fun onError(i: Int) {}

      override fun onResults(results: Bundle) {
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        // trigger to active standby mode use "test"
        if (matches != null) {
          if (isActivated) {
            isActivated = false
            speech.stopListening()
          } else {
            matches.firstOrNull {
              it.contains(activationKeyword, true)
            }.let {
              isActivated = true
              isPressed = !isPressed // reverse
              onRecord(isPressed)
              changeRecordButton(isPressed)
            }
            speech.startListening(speechRecognizerIntent)
          }
        }
      }

      override fun onPartialResults(bundle: Bundle) {}

      override fun onEvent(i: Int, bundle: Bundle) {}
    })

  }

  override fun onStop() {
    super.onStop()
    recorder?.release()
    recorder = null
    speech.destroy()
  }

  companion object {
    private const val LOG_AUDIO_TAG = "AudioRecordTest"
  }
}