package com.ralphmarondev.dragonfly.features.auth.presentation.account

import com.ralphmarondev.dragonfly.core.domain.model.User

data class AccountState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val showErrorMessage: Boolean = false,
    val navigateBack: Boolean = false
)