package com.guardian.guardianapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputEditText
import com.guardian.guardianapp.MainViewModel
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityRegisterContactsBinding
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.source.remote.response.DataItem
import com.guardian.guardianapp.ui.adapter.RegisterContactAdapter
import com.guardian.guardianapp.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class RegisterContactsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityRegisterContactsBinding
  private lateinit var viewModel: MainViewModel
  private lateinit var adapter: RegisterContactAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterContactsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    adapter = RegisterContactAdapter()
    setupRecycleView()
    setViewModel()
    setListContact()
    btnListener()
    setupToolbar()
    showLoading()
  }

  private fun btnListener() {
    adapter.setOnItemClickCallback(object : RegisterContactAdapter.OnItemClickCallback {
      override fun onItemClicked(data: DataItem) {
        viewModel.getUser().observe(this@RegisterContactsActivity) {
          viewModel.delContactUser(it.userid, data.id, it.token)
          viewModel.getResultResponse().observe(this@RegisterContactsActivity) { result ->
            if (result) {
              Helper.showToastShort(
                this@RegisterContactsActivity,
                getString(R.string.contact_deleted)
              )
            } else {
              Helper.showToastShort(
                this@RegisterContactsActivity,
                getString(R.string.contact_delete_failed)
              )
            }
          }
        }
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
        adapter.notifyDataSetChanged()
      }
    })

    binding.btnAddContact.setOnClickListener {
      addDialog()
    }
  }

  private fun addDialog() {

    val dialog = MaterialDialog(this)
      .noAutoDismiss()
      .customView(R.layout.add_contact_dialog)
    val buttonSave = dialog.findViewById(R.id.btn_save) as Button
    val inputPhone = dialog.findViewById(R.id.inputTextPhoneDialog) as TextInputEditText
    val inputName = dialog.findViewById(R.id.inputTextNameDialog) as TextInputEditText
    dialog.show()

    buttonSave.setOnClickListener {
      viewModel.getUser().observe(this) {
        viewModel.addContact(
          it.userid,
          it.token,
          inputName.text.toString(),
          inputPhone.text.toString()
        )
      }
      viewModel.getResultResponse().observe(this) { result ->
        if (result) {
          Helper.showToastShort(this, getString(R.string.contact_added))
        } else {
          Helper.showToastShort(this, getString(R.string.failed_add_contact))
        }
      }
      dialog.dismiss()
      finish()
      overridePendingTransition(0, 0)
      startActivity(intent)
      overridePendingTransition(0, 0)
      adapter.notifyDataSetChanged()
    }

    val buttonCancel = dialog.findViewById(R.id.btn_cancel) as Button
    buttonCancel.setOnClickListener {
      dialog.dismiss()
    }
    dialog.show()
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    binding.toolbar.setNavigationIcon(R.drawable.ic_back_grey)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  private fun setViewModel() {
    viewModel = ViewModelProvider(
      this,
      ViewModelFactory(UserPreference.getInstance(dataStore))
    )[MainViewModel::class.java]
  }

  private fun setupRecycleView() {
    binding.apply {
      rvUsers.layoutManager = LinearLayoutManager(this@RegisterContactsActivity)
      rvUsers.setHasFixedSize(true)
      rvUsers.adapter = adapter
    }
  }

  private fun setListContact() {
    viewModel.getUser().observe(this) {
      viewModel.getListContactUser(it.userid, it.token)
      viewModel.itemUserContact.observe(this) { data ->
        adapter.setListContact(data)
      }
    }
  }

  private fun showLoading() {
    viewModel.isLoading.observe(this) {
      if (it) {
        binding.progressBar.visibility = View.VISIBLE
      } else {
        binding.progressBar.visibility = View.GONE
      }
    }
  }
}