package com.ralphmarondev.dragonfly.features.auth.presentation.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralphmarondev.dragonfly.core.domain.model.Result
import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    init {
        loadInformation()
    }

    fun onAction(action: AccountAction) {
        when (action) {
            AccountAction.ClearNavigation -> {
                _state.update { it.copy(navigateBack = false) }
            }

            AccountAction.NavigateBack -> {
                _state.update { it.copy(navigateBack = true) }
            }

            AccountAction.Refresh -> {
                loadInformation(isRefreshing = true)
            }
        }
    }

    private fun loadInformation(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            try {
                Log.d("Account", "Load Information...")
                _state.update {
                    it.copy(
                        isLoading = true, isRefreshing = isRefreshing,
                        errorMessage = null,
                        showErrorMessage = false
                    )
                }

                when (val result = repository.getAccountInformation()) {
                    is Result.Success -> {
                        Log.d("Account", "Success. Email: ${result.data.email}")
                        _state.update { it.copy(user = result.data) }
                    }

                    is Result.Error -> {
                        Log.e("Account", "Error from result: ${result.message}")
                        _state.update {
                            it.copy(errorMessage = result.message, showErrorMessage = true)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("Account", "Load information error: ${e.message}")
                _state.update {
                    it.copy(errorMessage = e.message, showErrorMessage = true)
                }
            } finally {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
            }
            Log.d("Account", "Load Information done...")
        }
    }
}