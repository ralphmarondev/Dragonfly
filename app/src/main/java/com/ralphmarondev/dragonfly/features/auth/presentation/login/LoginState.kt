package com.ralphmarondev.dragonfly.features.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoggingIn: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val showErrorMessage: Boolean = false,
    val navigateToRegister: Boolean = false
)