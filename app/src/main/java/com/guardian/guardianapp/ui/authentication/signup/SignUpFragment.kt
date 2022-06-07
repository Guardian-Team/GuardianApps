package com.guardian.guardianapp.ui.authentication.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSignUpBinding
import com.guardian.guardianapp.ui.authentication.AuthActivity
import com.guardian.guardianapp.utils.Helper

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        (activity as AuthActivity).setUpToolbarVisibility(true)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        setValidationEdt()
        setBtnListener()
        showLoading()

        return binding.root
    }

    private fun setValidationEdt() {
        binding.apply {
            inputTextName.doOnTextChanged { text, _, _, _ ->
                if (text == null) {
                    nameInputLayout.error = getString(R.string.required)
                } else {
                    nameInputLayout.error = null
                }
            }

            inputTextPhone.doOnTextChanged { text, _, _, _ ->
                if (text == null) {
                    phoneInputLayout.error = getString(R.string.required)
                } else {
                    phoneInputLayout.error = null
                }
            }

            inputTextPass.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.length < 8) {
                        passInputLayout.error = getString(R.string.invalid_password)
                    } else {
                        passInputLayout.error = null
                    }
                } else {
                    passInputLayout.error = getString(R.string.required)
                }
            }

            inputTextEmail.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (isValid(text)) {
                        emailinputLayout.error = null
                    } else {
                        emailinputLayout.error = getString(R.string.invalid_email)
                    }
                } else {
                    emailinputLayout.error = getString(R.string.required)
                }
            }
        }
    }

    private fun isValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setBtnListener() {
        binding.apply {
            btnSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentSignIn)
                (activity as AuthActivity).setUpToolbarVisibility(false)
            }
            btnSignup.setOnClickListener {
                val username = binding.inputTextName.text.toString()
                val email = binding.inputTextEmail.text.toString()
                val password = binding.inputTextPass.text.toString()
                val phone = binding.inputTextPhone.text.toString()
                if (validateInputText()
                ) {
                    viewModel.postRegister(username, email, password, phone)
                    viewModel.getResultResponse().observe(viewLifecycleOwner) {
                        if (it == true) {
                            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentSignIn)
                            Helper.showToastLong(activity, getString(R.string.acc_created))
                        }else{
                            Helper.showToastLong(activity, getString(R.string.acc_exists))
                        }
                    }
                }else{
                    Helper.showToastLong(activity, getString(R.string.field_regis_empty))
                }
            }
        }
    }

    private fun validateInputText(): Boolean {
        return binding.inputTextName.text.toString()
            .isNotEmpty() && binding.inputTextEmail.text.toString()
            .isNotEmpty() && binding.inputTextPass.text.toString().isNotEmpty()
    }

    private fun showLoading(){
        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}