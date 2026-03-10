package com.ralphmarondev.dragonfly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import com.ralphmarondev.dragonfly.core.presentation.theme.DragonflyTheme
import com.ralphmarondev.dragonfly.core.presentation.theme.LocalThemeState
import com.ralphmarondev.dragonfly.core.presentation.theme.ThemeProvider
import com.ralphmarondev.dragonfly.navigation.AppNavigation
import com.ralphmarondev.dragonfly.navigation.Routes
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferences: AppPreferences by inject()

            ThemeProvider(preferences = preferences) {
                val themeState = LocalThemeState.current
                var startDestination by rememberSaveable { mutableStateOf<Routes?>(null) }
                DragonflyTheme(darkTheme = themeState.darkTheme.value) {

                    LaunchedEffect(Unit) {
                        val isAuthenticated = preferences.isAuthenticated().first()
                        startDestination = if (isAuthenticated) Routes.Dashboard else Routes.Login
                    }

                    startDestination?.let { destination ->
                        AppNavigation(startDestination = destination)
                    }
                }
            }
        }
    }
}