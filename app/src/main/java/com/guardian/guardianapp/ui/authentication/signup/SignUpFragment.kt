package com.guardian.guardianapp.ui.authentication.signup

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.findNavController
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentSignUpBinding
import com.guardian.guardianapp.ui.authentication.AuthActivity
import com.guardian.guardianapp.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("users")

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
          if (text.length < 6) {
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
        findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentSignIn)
        Helper.showToastLong(activity, getString(R.string.acc_created))
      }
    }
  }
}