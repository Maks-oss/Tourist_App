package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).hideBottomNavigationBar()
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
            LoginUtils.isLoginValid(login)
        val password = fragmentLoginBinding.passwordInput.text.toString()
        val passwordValid =
            LoginUtils.isPasswordValid(password)
        applyLoginValidation(loginValid)
        applyPasswordValidation(passwordValid)
        Log.d("TAG", "processLogin: ${(login.hashCode() + password.hashCode()).toLong()}")
        userViewModel.applyUser((login.hashCode() + password.hashCode()).toLong())
        setupObserver(loginValid && passwordValid)
    }

    private fun setupObserver(isValid: Boolean) {
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (isValid) {
                if (it != null) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSearchFragment())
                } else {
                    requireContext().showMessage(getString(R.string.user_not_exist_message))
                }
            }
        }
    }

    private fun applyPasswordValidation(passwordValid: Boolean) {
        if (passwordValid) {
            fragmentLoginBinding.passwordLayout.error = null
        } else {
            fragmentLoginBinding.passwordLayout.error =
                getString(R.string.password_error)
        }
    }

    private fun applyLoginValidation(loginValid: Boolean) {
        if (loginValid) {
            fragmentLoginBinding.loginLayout.error = null
        } else {
            fragmentLoginBinding.loginLayout.error =
                getString(R.string.login_error)
        }
    }
}