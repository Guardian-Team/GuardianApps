package com.guardian.guardianapp.ui.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSignUpBinding
import com.guardian.guardianapp.ui.authentication.AuthActivity

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        (activity as AuthActivity).setUpToolbarVisibility(true)
        setValidationEdt()
        setBtnListener()

        return binding.root
    }


    private fun setValidationEdt(){
        binding.apply {
            inputtextname.doOnTextChanged { text, _, _, _ ->
                if (text == null){
                    binding.nameinputlayout.error = getString(R.string.required)
                }else{
                    binding.nameinputlayout.error = null
                }
            }

            inputtextphone.doOnTextChanged { text, _, _, _ ->
                if (text == null){
                    binding.phoneinputlayout.error = getString(R.string.required)
                }else{
                    binding.phoneinputlayout.error = null
                }
            }
            inputtextpass.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.length < 6 ){
                        binding.passinputlayout.error = getString(R.string.invalid_password)
                    }else{
                        binding.passinputlayout.error = null
                    }
                }else{
                    binding.passinputlayout.error = getString(R.string.required)
                }
            }
            inputtextemail.doOnTextChanged { text, _, _, _ ->
                if (text != null){
                    if(isValid(text)){
                        binding.emailinputlayout.error = null
                    }else{
                        binding.emailinputlayout.error = getString(R.string.invalid_email)
                    }
                }else{
                    binding.emailinputlayout.error = getString(R.string.required)
                }
            }
        }
    }

    private fun isValid(email: CharSequence): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setBtnListener(){
        binding.apply {
            btnSignin.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSignup_to_fragmentSignin)
                (activity as AuthActivity).setUpToolbarVisibility(false)
            }
            btnSignup.setOnClickListener {
                Toast.makeText(activity, getString(R.string.acc_created), Toast.LENGTH_LONG).show()
            }
        }
    }
}