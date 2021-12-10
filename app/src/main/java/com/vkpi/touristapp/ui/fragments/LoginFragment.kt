package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vkpi.touristapp.R
import com.vkpi.touristapp.databinding.FragmentLoginBinding
import com.vkpi.touristapp.ui.MainActivity
import com.vkpi.touristapp.utils.LoginUtils
import com.vkpi.touristapp.utils.showMessage
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import com.vkpi.touristapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userViewModel : UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? MainActivity)?.hideBottomNavigationBar()
        userViewModel=ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        fragmentLoginBinding.btnLogin.setOnClickListener {
            processLogin()
        }
        fragmentLoginBinding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
    }

    private fun processLogin() {
        val login = fragmentLoginBinding.loginInput.text.toString()
        val loginValid =
            LoginUtils.isValidInput(login)
        val password = fragmentLoginBinding.passwordInput.text.toString()
        val passwordValid =
            LoginUtils.isValidInput(password)
        applyLoginValidation(loginValid)
        applyPasswordValidation(passwordValid)
        applyLoginAction(
            loginValid && passwordValid,
            (login.hashCode() + password.hashCode()).toLong()
        )
    }

    private fun applyLoginAction(isValid: Boolean, userId: Long) {
        lifecycleScope.launch {
            if (isValid) {
                if (userViewModel.isUserExist(userId)) {
                    userViewModel.applyUserId(userId)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSearchFragment())
                } else {
                    requireView().showMessage(getString(R.string.user_not_exist_message))
                }
            }
        }

    }

    private fun applyPasswordValidation(passwordValid: Boolean) {
        fragmentLoginBinding.passwordLayout.error = if (passwordValid) {
            null
        } else {
            getString(R.string.password_error)
        }
    }

    private fun applyLoginValidation(loginValid: Boolean) {
        fragmentLoginBinding.loginLayout.error = if (loginValid) {
            null
        } else {
            getString(R.string.login_error)
        }
    }
}