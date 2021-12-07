package com.vkpi.touristapp.utils

object LoginUtils {
    fun isPasswordValid(password: String): Boolean = password.length >= 4
    fun isLoginValid(login: String): Boolean = login.length >= 4
}