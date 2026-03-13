package com.ralphmarondev.dragonfly.features.dashboard.presentation

import com.ralphmarondev.dragonfly.core.domain.model.Location

data class DashboardState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showErrorMessage: Boolean = false,
    val locations: List<Location> = emptyList(),
    val navigateToAccount: Boolean = false
)