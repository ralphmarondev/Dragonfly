package com.ralphmarondev.dragonfly.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.DisplayNameChange -> {
                _state.update { it.copy(displayName = action.displayName) }
            }

            is RegisterAction.VehicleIdChange -> {
                _state.update { it.copy(vehicleId = action.vehicleId) }
            }

            is RegisterAction.EmailChange -> {
                _state.update { it.copy(email = action.email) }
            }

            is RegisterAction.PasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }

            is RegisterAction.ConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.confirmPassword) }
            }

            RegisterAction.Login -> {
                _state.update { it.copy(navigateToLogin = true) }
            }

            RegisterAction.Register -> {
                register(
                    displayName = _state.value.displayName.trim(),
                    vehicleId = _state.value.vehicleId.trim(),
                    email = _state.value.email.trim(),
                    password = _state.value.password.trim(),
                    confirmPassword = _state.value.confirmPassword.trim()
                )
            }

        }
    }

    private fun register(
        displayName: String,
        vehicleId: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isRegistering = true,
                        isRegistered = false,
                        errorMessage = null,
                        showErrorMessage = false
                    )
                }

                if (displayName.isBlank()) {
                    _state.update {
                        it.copy(
                            errorMessage = "Display name cannot be blank.",
                            showErrorMessage = true
                        )
                    }
                    return@launch
                }
                if (displayName.isBlank()) {
                    _state.update {
                        it.copy(
                            errorMessage = "Vehicle ID cannot be blank.",
                            showErrorMessage = true
                        )
                    }
                    return@launch
                }
                if (email.isBlank()) {
                    _state.update {
                        it.copy(
                            errorMessage = "Email cannot be blank.",
                            showErrorMessage = true
                        )
                    }
                    return@launch
                }
                if (password.isBlank()) {
                    _state.update {
                        it.copy(
                            errorMessage = "Password cannot be blank.",
                            showErrorMessage = true
                        )
                    }
                    return@launch
                }
                if (confirmPassword != password) {
                    _state.update {
                        it.copy(
                            errorMessage = "Passwords did not match.",
                            showErrorMessage = true
                        )
                    }
                    return@launch
                }

                val registerResult = repository.register(
                    displayName = displayName,
                    vehicleId = vehicleId,
                    email = email,
                    password = password
                )

                when (registerResult) {
                    true -> {
                        _state.update { it.copy(isRegistered = true) }
                    }

                    false -> {
                        _state.update {
                            it.copy(
                                errorMessage = "Registration failed.",
                                showErrorMessage = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = e.localizedMessage ?: "Registration failed.",
                        showErrorMessage = true
                    )
                }
            } finally {
                _state.update { it.copy(isRegistering = false) }
            }
        }
    }
}