package com.ralphmarondev.dragonfly.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralphmarondev.dragonfly.features.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        loadLocations()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigateToAccount -> {
                _state.update { it.copy(navigateToAccount = true) }
            }

            DashboardAction.ClearNavigation -> {
                _state.update { it.copy(navigateToAccount = false) }
            }

            DashboardAction.Refresh -> {
                loadLocations(isRefreshing = true)
            }
        }
    }

    private fun loadLocations(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, isRefreshing = isRefreshing) }

                val locations = repository.getLastLocations()
                if (isRefreshing) delay(1500)
                _state.update { it.copy(locations = locations) }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message, showErrorMessage = true) }
            } finally {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }
}