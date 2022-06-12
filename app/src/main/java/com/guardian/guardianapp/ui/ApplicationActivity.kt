package com.guardian.guardianapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityApplicationBinding
import com.guardian.guardianapp.model.SettingsModel
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

class ApplicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplicationBinding
    private lateinit var viewModel: ApplicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setViewModel()
        btnListener()
        showSosMsg()
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

    private fun btnListener() {
        binding.apply {
            registeredContacts.setOnClickListener {
                startActivity(
                    Intent(
                        this@ApplicationActivity,
                        RegisterContactsActivity::class.java
                    )
                )
            }
            btnSaveMsg.setOnClickListener {
                if (textSos.text != null) {
                    saveSosMsg()
                    Helper.showToastShort(this@ApplicationActivity, getString(R.string.msg_saved))
                    finish()
                    startActivity(intent)
                } else {
                    Helper.showToastShort(
                        this@ApplicationActivity,
                        getString(R.string.fill_first_msg)
                    )
                }
            }
            toggleDarkMode.setOnClickListener {
                Helper.showToastShort(this@ApplicationActivity, getString(R.string.available_soon))
            }

        }

    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[ApplicationViewModel::class.java]

    }

    private fun showSosMsg() {
        viewModel.getUserSettings().observe(this) {
            if (it.msgSos != "") {
                binding.textSos.setText(it.msgSos)
            }
        }
    }

    private fun saveSosMsg() {
        val model = SettingsModel(
            binding.textSos.text.toString()
        )
        viewModel.saveUserSettings(model)
    }

}