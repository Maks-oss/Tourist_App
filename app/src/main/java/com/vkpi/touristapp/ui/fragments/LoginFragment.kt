package com.vkpi.touristapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vkpi.touristapp.R
import com.vkpi.touristapp.databinding.FragmentLoginBinding
import com.vkpi.touristapp.ui.MainActivity
import com.vkpi.touristapp.utils.LoginUtils
import com.vkpi.touristapp.viewmodels.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private val placeViewModel by viewModels<PlaceViewModel>()
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
        val loginValid =
            LoginUtils.isLoginValid(fragmentLoginBinding.loginInput.text.toString())
        val passwordValid =
            LoginUtils.isPasswordValid(fragmentLoginBinding.passwordInput.text.toString())
        if (loginValid) {
            fragmentLoginBinding.loginLayout.error = null
        } else {
            fragmentLoginBinding.loginLayout.error =
                getString(R.string.login_error)
        }
        if (passwordValid) {
            fragmentLoginBinding.passwordLayout.error = null
        } else {
            fragmentLoginBinding.passwordLayout.error =
                getString(R.string.password_error)
        }
        if (loginValid && passwordValid) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSearchFragment())
        }
    }
}