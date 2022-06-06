package com.guardian.guardianapp.ui.authentication.signin

import android.content.Intent
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
import com.guardian.guardianapp.databinding.FragmentSignInBinding
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.ui.HomeActivity
import com.guardian.guardianapp.ui.authentication.AuthActivity
import com.guardian.guardianapp.utils.Helper


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        setBtnListener()
        setValidationEdt()
        setViewModel()
        showLoading()

        return binding.root
    }

    private fun setValidationEdt() {
        binding.apply {
            inputTextPass.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.length < 6) {
                        passwordInputLayout.error = getString(R.string.invalid_password)
                    } else {
                        passwordInputLayout.error = null
                    }
                } else {
                    passwordInputLayout.error = getString(R.string.required)
                }
            }

            inputTextEmail.doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (isValid(text)) {
                        emailInputLayout.error = null
                    } else {
                        emailInputLayout.error = getString(R.string.invalid_email)
                    }
                } else {
                    emailInputLayout.error = getString(R.string.required)
                }
            }
        }
    }

    private fun setBtnListener() {
        binding.apply {
            btnSignIn.setOnClickListener {
                val email = binding.inputTextEmail.text.toString()
                val password = binding.inputTextPass.text.toString()
                if (validateInputText()){
                    viewModel.postLogin(email, password)
                    viewModel.getResultResponse().observe(viewLifecycleOwner){
                        if(it){
                            startActivity(Intent(activity, HomeActivity::class.java))
                            activity?.finishAffinity()
                        }else{
                            Helper.showToastLong(activity, "Please Enter the correct email or password")
                        }
                    }
                }else{
                    Helper.showToastLong(activity, getString(R.string.field_login_empty))
                }
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
            }
            btnGoogle.setOnClickListener {
                Helper.showToastLong(activity, "Available soon!")
            }
            btnFacebook.setOnClickListener {
                Helper.showToastLong(activity, "Available soon!")
            }
            containerForgot.setOnClickListener{
                Helper.showToastLong(activity, "Available soon!")
            }
        }
    }

    private fun isValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setViewModel() {
        val pref = UserPreference.getInstance((activity as AuthActivity).getDataStore())
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[SignInViewModel::class.java]
    }

    private fun validateInputText(): Boolean {
        return binding.inputTextEmail.text.toString()
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