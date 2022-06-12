package com.guardian.guardianapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ItemRowUserBinding
import com.guardian.guardianapp.source.remote.response.DataItem

class RegisterContactAdapter : RecyclerView.Adapter<RegisterContactAdapter.ViewHolder>() {
    private val listContact = ArrayList<DataItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface  OnItemClickCallback{
        fun onItemClicked(data: DataItem)
    }

    fun setListContact(itemContact: List<DataItem>){
        listContact.clear()
        listContact.addAll(itemContact)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegisterContactAdapter.ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegisterContactAdapter.ViewHolder, position: Int) {
        holder.bind(listContact[position])
    }

    override fun getItemCount() = listContact.size


    inner class ViewHolder(private var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: DataItem) {
            with(binding){
                imageUser.setImageResource(R.drawable.ic_account_outline)
                name.text = contact.name
                noPhone.text = contact.phone
                idUserContact.text = contact.id.toString()

                btnDelete.setOnClickListener {
                    onItemClickCallback?.onItemClicked(contact)
                }
            }
        }

    }
}