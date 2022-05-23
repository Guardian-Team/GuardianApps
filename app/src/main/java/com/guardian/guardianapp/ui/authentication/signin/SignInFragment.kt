package com.guardian.guardianapp.ui.authentication.signin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.HomeActivity
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSignInBinding
import com.guardian.guardianapp.ui.authentication.AuthActivity

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        setBtnListener()
        setValidationEdt()

        return binding.root
    }

    private fun setValidationEdt(){
        binding.apply {
            inputtextpass.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.length < 6 ){
                        binding.passwordInpuLayout.error = getString(R.string.invalid_password)
                    }else{
                        binding.passwordInpuLayout.error = null
                    }
                }else{
                    binding.passwordInpuLayout.error = getString(R.string.required)
                }
            }
            inputtextemail.doOnTextChanged { text, _, _, _ ->
                if (text != null){
                    if(isValid(text)){
                        binding.emailInputLayout.error = null
                    }else{
                        binding.emailInputLayout.error = getString(R.string.invalid_email)
                    }
                }else {
                    binding.emailInputLayout.error = getString(R.string.required)
                }
            }
        }
    }

    private fun setBtnListener(){
        binding.apply {
            btnSignin.setOnClickListener{
                startActivity(Intent(activity, HomeActivity::class.java))
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSignin_to_fragmentSignup)
            }
            btnGoogle.setOnClickListener {
                Toast.makeText(activity, "Google Pressed!", Toast.LENGTH_LONG).show()
            }
            btnFacebook.setOnClickListener {
                Toast.makeText(activity, "Facebook Pressed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValid(email: CharSequence): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}