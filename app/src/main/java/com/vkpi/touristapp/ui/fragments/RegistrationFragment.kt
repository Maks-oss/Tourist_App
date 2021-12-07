package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vkpi.touristapp.R
import com.vkpi.touristapp.database.entities.User
import com.vkpi.touristapp.databinding.FragmentRegistrationBinding
import com.vkpi.touristapp.utils.LoginUtils
import com.vkpi.touristapp.utils.showMessage
import com.vkpi.touristapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private lateinit var registrationBinding: FragmentRegistrationBinding
    private val userViewModel by activityViewModels<UserViewModel>()
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

        val login = registrationBinding.loginInput.text.toString()
        val password = registrationBinding.passwordInput.text.toString()
        val userId = login.hashCode() + password.hashCode()
        val loginValid =
            LoginUtils.isValidInput(login)
        val passwordValid =
            LoginUtils.isValidInput(password)
        val repeatPasswordValid =
            password == registrationBinding.passwordRepaetNput.text.toString()

        applyLoginValidation(loginValid)
        applyPasswordValidation(passwordValid)
        applyRepeatPasswordValidation(repeatPasswordValid)
        applyRegisterAction(
            loginValid && passwordValid && repeatPasswordValid,
            login,
            password,
            userId.toLong()
        )

    }

    private fun applyRegisterAction(
        isValid: Boolean,
        login: String,
        password: String,
        userId: Long
    ) {
        lifecycleScope.launch {
            if (isValid) {
                if (!userViewModel.isUserExist(userId)) {
                    userViewModel.insertUserIntoDb(
                        User(
                            userId = userId,
                            login = login,
                            password = password
                        )
                    )
                    userViewModel.applyUserId(userId)
                    findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToSearchFragment())
                } else {
                    requireContext().showMessage(getString(R.string.existing_user_message))
                }
            }
        }
    }

    private fun applyRepeatPasswordValidation(repeatPasswordValid: Boolean) {
        registrationBinding.repeatPasswordLayout.error = if (!repeatPasswordValid) {
            getString(R.string.repeat_password_error)
        } else {
            null
        }
    }

    private fun applyPasswordValidation(passwordValid: Boolean) {
        registrationBinding.passwordLayout.error = if (passwordValid) {
            null
        } else {
            getString(R.string.password_error)
        }
    }

    private fun applyLoginValidation(loginValid: Boolean) {
        registrationBinding.loginLayout.error = if (loginValid) {
            null
        } else {
            getString(R.string.login_error)
        }
    }
}