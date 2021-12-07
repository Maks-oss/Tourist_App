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
            LoginUtils.isLoginValid(login)
        val passwordValid =
            LoginUtils.isPasswordValid(password)
        val repeatPasswordValid =
            password == registrationBinding.passwordRepaetNput.text.toString()

        applyLoginValidation(loginValid)
        applyPasswordValidation(passwordValid)
        applyRepeatPasswordValidation(repeatPasswordValid)
        userViewModel.applyUser(userId.toLong())
        setupObserver(loginValid && passwordValid && repeatPasswordValid,login,password,userId.toLong())

    }

    private fun setupObserver(isValid: Boolean, login: String, password: String, userId: Long) {
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            Log.d("TAG", "setupObserver: $it")
            if (isValid) {
                if (it == null) {
                    val user = User(
                        userId = userId,
                        login = login,
                        password = password
                    )
                    userViewModel.insertUserIntoDb(
                        user
                    )
                    userViewModel.applyUser(user)
                    findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToSearchFragment())

                } else {
                    requireContext().showMessage(getString(R.string.existing_user_message))
                }
            } else {
                requireContext().showMessage(getString(R.string.text_fields_message))
            }
        }
    }


    private fun applyRepeatPasswordValidation(repeatPasswordValid: Boolean) {
        if (!repeatPasswordValid) {
            registrationBinding.repeatPasswordLayout.error =
                getString(R.string.repeat_password_error)
        } else {
            registrationBinding.repeatPasswordLayout.error = null
        }
    }

    private fun applyPasswordValidation(passwordValid: Boolean) {
        if (passwordValid) {
            registrationBinding.passwordLayout.error = null
        } else {
            registrationBinding.passwordLayout.error =
                getString(R.string.password_error)
        }
    }

    private fun applyLoginValidation(loginValid: Boolean) {
        if (loginValid) {
            registrationBinding.loginLayout.error = null
        } else {
            registrationBinding.loginLayout.error =
                getString(R.string.login_error)
        }
    }
}