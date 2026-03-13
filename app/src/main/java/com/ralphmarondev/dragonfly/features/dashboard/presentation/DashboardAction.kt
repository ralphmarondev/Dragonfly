package com.ralphmarondev.dragonfly.features.dashboard.presentation

sealed interface DashboardAction {
    data object NavigateToAccount : DashboardAction
    data object ClearNavigation : DashboardAction
    data object Refresh : DashboardAction
}