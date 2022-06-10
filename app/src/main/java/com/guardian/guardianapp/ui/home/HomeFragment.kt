package com.guardian.guardianapp.ui.home

import android.Manifest
import android.content.Intent
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
import com.guardian.guardianapp.utils.WavRecorder
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
  private val speech: SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(activity) }
  private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

  private var isActivated: Boolean = false
  private val activationKeyword: String = "test"

  private lateinit var fileName: String
  private var isPressed: Boolean = false

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private lateinit var wavRecord: WavRecorder

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
    wavRecord = WavRecorder(requireActivity().baseContext)
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
      "${activity?.getExternalFilesDir("/")?.absolutePath}/Record_${formatter.format(now)}.wav"
  }

  private fun onRecord(start: Boolean) = if (start) {
    startRecording()
  } else {
    stopRecording()
    detectViolence()
  }

  private fun startRecording() {
    setFileName()
    binding.chronometer.base = SystemClock.elapsedRealtime()
    binding.chronometer.start()

    wavRecord.startRecording()
    Log.d(LOG_AUDIO_TAG, "Start Recording")
  }


  private fun stopRecording() {
    binding.chronometer.stop()

    wavRecord.stopRecording()
    Log.d(LOG_AUDIO_TAG, "Stop Recording")
    Log.d(LOG_AUDIO_TAG, "Detect Success")
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

  private fun detectViolence(){
    val mfcc = Helper.mfcc(requireActivity(), fileName)
    Log.d("Result ByteArray", mfcc.toString())

    val byteBuffer = ByteBuffer.wrap(mfcc)
    Log.d("Result ByteBuffer", byteBuffer.toString())

    val detection = Helper.audioDetection(requireActivity(), byteBuffer)

    /**
     * Index of Array for detect 4 types of violence
     * 0- sexual
     * 1- stalking
     * 2- physical
     * 3- domestic
     */
    val temp = listOf(detection[0],
      detection[1],
      detection[2],
      detection[3]
    )
//    val result = temp.maxOrNull() ?: 0
    val index = temp.maxOrNull().let {
      temp.indexOf(it)
    }
    Log.d("Result AudioDetection sexual", detection[0].toString())
    Log.d("Result AudioDetection stalking", detection[1].toString())
    Log.d("Result AudioDetection physical", detection[2].toString())
    Log.d("Result AudioDetection domestic", detection[3].toString())

    lateinit var message : String
    when(index){
      0 -> message = "Sexual"
      1 -> message = "Stalking"
      2 -> message = "Physical"
      3 -> message = "Domestic"
    }
    Helper.showToastLong(requireActivity(), message)
  }

  override fun onStop() {
    super.onStop()
    wavRecord.stopRecording()
    speech.destroy()
  }

  companion object {
    private const val LOG_AUDIO_TAG = "AudioRecordTest"
  }
}