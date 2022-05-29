package com.guardian.guardianapp.ui.authentication.signin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSignInBinding
import com.guardian.guardianapp.ui.HomeActivity
import com.guardian.guardianapp.utils.Helper

class SignInFragment : Fragment() {

  private lateinit var signInViewModel: SignInViewModel

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
        startActivity(Intent(activity, HomeActivity::class.java))
      }
      btnSignup.setOnClickListener {
        findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
      }
      btnGoogle.setOnClickListener {
        Helper.showToastLong(activity, "Google Pressed!")
      }
      btnFacebook.setOnClickListener {
        Helper.showToastLong(activity, "Facebook Pressed!")
      }
    }
  }

  private fun isValid(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

}