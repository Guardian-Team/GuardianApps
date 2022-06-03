package com.guardian.guardianapp.ui.adapter

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guardian.guardianapp.databinding.ItemRowAudioBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AudioAdapter(
  private val files: Array<File>?,
  private val listener: OnItemClickCallback
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

  private val metaRetriever = MediaMetadataRetriever()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
    val binding = ItemRowAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    return AudioViewHolder(binding)
  }

  override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
    holder.bind(files!![position])
  }

  override fun getItemCount(): Int = files?.size!!

  interface OnItemClickCallback {
    fun onBtnPlayClicked(data: File, bindingAdapter :ItemRowAudioBinding)
    fun onBtnDeleteClicked(data: File)
  }

  inner class AudioViewHolder(private val binding: ItemRowAudioBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(file: File) {
      with(binding) {
        tvAudioName.text = file.name

        metaRetriever.setDataSource(file.absolutePath)

        // convert duration to minute:seconds
        val duration =
          metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
        val tempString =
          Date(file.lastModified()).toString().split("GMT")[0] +
            " | ${SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(duration))}"
        tvTime.text = tempString

        btnPlayAudio.setOnClickListener {
          listener.onBtnPlayClicked(file, binding)
        }
        btnDelete.setOnClickListener {
          listener.onBtnDeleteClicked(file)
        }
      }
    }
  }
}