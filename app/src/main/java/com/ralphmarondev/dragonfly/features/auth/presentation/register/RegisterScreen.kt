package com.ralphmarondev.dragonfly.features.auth.presentation.register

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ralphmarondev.dragonfly.core.presentation.components.NormalTextField
import com.ralphmarondev.dragonfly.core.presentation.components.PasswordTextField
import com.ralphmarondev.dragonfly.core.presentation.components.PrimaryButton
import com.ralphmarondev.dragonfly.core.presentation.components.SecondaryButton
import com.ralphmarondev.dragonfly.core.presentation.theme.LocalThemeState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreenRoot(
    onLogin: () -> Unit,
    onRegisterSuccessful: () -> Unit
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.navigateToLogin) {
        if (state.navigateToLogin) {
            onLogin()
        }
    }

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onRegisterSuccessful()
        }
    }

    RegisterScreen(
        state = state,
        action = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    state: RegisterState,
    action: (RegisterAction) -> Unit
) {
    val themeState = LocalThemeState.current
    val darkMode by themeState.darkTheme

    val focusManager = LocalFocusManager.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showErrorMessage) {
        if (state.showErrorMessage) {
            snackbar.showSnackbar(
                message = state.errorMessage ?: "Registration Failed."
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Register")
                },
                actions = {
                    IconButton(onClick = themeState::toggleTheme) {
                        Icon(
                            imageVector = if (darkMode) {
                                Icons.Outlined.LightMode
                            } else {
                                Icons.Outlined.DarkMode
                            },
                            contentDescription = if (darkMode) {
                                "Switch to light mode"
                            } else {
                                "Switch to dark mode"
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Join Dragonfly Community",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Create an account to continue.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                NormalTextField(
                    value = state.displayName,
                    onValueChange = { action(RegisterAction.DisplayNameChange(it)) },
                    leadingIconImageVector = Icons.Outlined.AccountTree,
                    labelText = "Display Name",
                    placeHolderText = "Your name",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                NormalTextField(
                    value = state.email,
                    onValueChange = { action(RegisterAction.EmailChange(it)) },
                    leadingIconImageVector = Icons.Outlined.Email,
                    labelText = "Email",
                    placeHolderText = "someone@example.com",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                PasswordTextField(
                    value = state.password,
                    onValueChange = { action(RegisterAction.PasswordChange(it)) },
                    leadingIconImageVector = Icons.Outlined.Password,
                    labelText = "Password",
                    placeholderText = "Enter password",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                PasswordTextField(
                    value = state.confirmPassword,
                    onValueChange = { action(RegisterAction.ConfirmPasswordChange(it)) },
                    leadingIconImageVector = Icons.Outlined.Password,
                    labelText = "Confirm Password",
                    placeholderText = "Re-enter password",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Register",
                    onClick = { action(RegisterAction.Register) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isRegistering
                )

                Spacer(modifier = Modifier.height(16.dp))
                SecondaryButton(
                    text = "Already have an Account",
                    onClick = { action(RegisterAction.Login) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}