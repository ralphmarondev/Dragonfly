package com.ralphmarondev.dragonfly.features.auth.presentation.register

data class RegisterState(
    val errorMessage: String? = null,
    val showErrorMessage: Boolean = false,
    val displayName: String = "",
    val vehicleId: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRegistering: Boolean = false,
    val isRegistered: Boolean = false,
    val navigateToLogin: Boolean = false
)