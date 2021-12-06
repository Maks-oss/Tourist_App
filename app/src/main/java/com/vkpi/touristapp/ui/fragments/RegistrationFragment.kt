package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vkpi.touristapp.R
import com.vkpi.touristapp.databinding.FragmentRegistrationBinding
import com.vkpi.touristapp.utils.LoginUtils

class RegistrationFragment : Fragment() {
    private lateinit var registrationBinding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registrationBinding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return registrationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registrationBinding.btnLogin.setOnClickListener {
            processRegistration()
        }
    }

    private fun processRegistration() {

        val loginValid =
            LoginUtils.isLoginValid(registrationBinding.loginInput.text.toString())
        val passwordValid =
            LoginUtils.isPasswordValid(registrationBinding.passwordInput.text.toString())
        val repeatPasswordValid =
            registrationBinding.passwordInput.text.toString() == registrationBinding.passwordRepaetNput.text.toString()
        if (loginValid) {
            registrationBinding.loginLayout.error = null
        } else {
            registrationBinding.loginLayout.error =
                getString(R.string.login_error)
        }
        if (passwordValid) {
            registrationBinding.passwordLayout.error = null
        } else {
            registrationBinding.passwordLayout.error =
                getString(R.string.password_error)
        }
        if (!repeatPasswordValid) {
            registrationBinding.repeatPasswordLayout.error = getString(R.string.repeat_password_error)
        } else {
            registrationBinding.repeatPasswordLayout.error = null
        }
        if (loginValid && passwordValid && repeatPasswordValid) {
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToSearchFragment())
        }
    }
}