package com.guardian.guardianapp.ui.history

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentHistoryBinding
import com.guardian.guardianapp.databinding.ItemRowAudioBinding
import com.guardian.guardianapp.ui.adapter.AudioAdapter
import com.guardian.guardianapp.utils.Helper
import com.guardian.guardianapp.utils.Helper.audioDetection
import com.guardian.guardianapp.utils.Helper.audioDetectionZupa
import com.guardian.guardianapp.utils.Helper.mfcc
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

class HistoryFragment : Fragment() {

  private var _binding: FragmentHistoryBinding? = null
  private val binding get() = _binding!!

  private lateinit var adapter: AudioAdapter

  private var player: MediaPlayer? = null
  private var isPlaying = false
  private lateinit var fileName: String

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val historyViewModel =
      ViewModelProvider(this)[HistoryViewModel::class.java]
    _binding = FragmentHistoryBinding.inflate(inflater, container, false)

    setDataHistory()

    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setDataHistory() {
    //get directory
    val path = "${activity?.getExternalFilesDir("/")?.absolutePath}"
    val files = File(path).listFiles()

    //when button play clicked
    adapter = AudioAdapter(files, object : AudioAdapter.OnItemClickCallback {
      override fun onBtnPlayClicked(data: File, bindingAdapter: ItemRowAudioBinding) {
        activity?.let { Helper.showToastShort(it, data.name) }
        isPlaying = !isPlaying
        fileName = data.absolutePath

        if (isPlaying) {
          bindingAdapter.btnPlayAudio.setImageResource(R.drawable.ic_pause)
          onPlay(true)
          //stop play when finish
          player?.setOnCompletionListener {
            stopPlaying()
            isPlaying = !isPlaying
            bindingAdapter.btnPlayAudio.setImageResource(R.drawable.ic_play)
          }
        } else {
          setDataHistory()
          bindingAdapter.btnPlayAudio.setImageResource(R.drawable.ic_play)
          onPlay(false)
        }
      }

      override fun onBtnDeleteClicked(data: File) {
        alertDialog(data)
      }
    })
    showIfNoAudio()
  }

  private fun alertDialog(data: File) {
    val dialog = Dialog(requireActivity())

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.alert_warning)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val buttonYesAlert = dialog.findViewById(R.id.btn_yes) as Button
    buttonYesAlert.setOnClickListener {
      data.delete()
      dialog.dismiss()
      setDataHistory()
      successDialog()
    }

    val buttonNoAlert = dialog.findViewById(R.id.btn_no) as Button
    buttonNoAlert.setOnClickListener {
      dialog.dismiss()
    }
    dialog.show()
  }

  private fun successDialog() {
    val dialog = Dialog(requireActivity())

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.alert_success)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(true)

    val btnClose = dialog.findViewById(R.id.btn_close_alert_success) as ImageButton
    btnClose.setOnClickListener {
      dialog.dismiss()
    }
    dialog.show()
  }

  private fun showIfNoAudio() { // show text if there's no audio file
    if (adapter.itemCount >= 1) {
      binding.rvAudioFiles.layoutManager = LinearLayoutManager(context)
      binding.rvAudioFiles.adapter = adapter
      binding.noAudioRecorded.visibility = View.GONE
      binding.rvAudioFiles.visibility = View.VISIBLE
    } else {
      binding.noAudioRecorded.visibility = View.VISIBLE
      binding.rvAudioFiles.visibility = View.GONE
    }
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
        Log.e(LOG_AUDIO_TAG, "prepare() failed $e")
      }
    }
  }

  private fun stopPlaying() {
    player?.release()
    player = null
  }

  override fun onStop() {
    super.onStop()
    player?.release()
    player = null
  }

  companion object {
    private const val LOG_AUDIO_TAG = "AudioPlayTest"
  }
}


