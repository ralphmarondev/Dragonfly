package com.ralphmarondev.dragonfly.features.auth.presentation.account

sealed interface AccountAction {
    data object NavigateBack : AccountAction
    data object ClearNavigation : AccountAction
    data object Refresh : AccountAction
}