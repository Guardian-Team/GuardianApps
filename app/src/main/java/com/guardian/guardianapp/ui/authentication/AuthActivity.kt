package com.guardian.guardianapp.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.ActivityAuthBinding
import com.guardian.guardianapp.databinding.ToolbarBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init navhostcontroler
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController
        setUpToolbar()
    }


    fun setUpToolbarVisibility(isSigup: Boolean){
        if (isSigup){
            binding.authToolbar.visibility = View.VISIBLE
        }else if(isSigup == false){
            binding.authToolbar.visibility = View.GONE
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding.authToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}