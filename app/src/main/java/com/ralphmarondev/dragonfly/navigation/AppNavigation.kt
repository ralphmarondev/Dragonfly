package com.ralphmarondev.dragonfly.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ralphmarondev.dragonfly.features.auth.presentation.account.AccountScreenRoot
import com.ralphmarondev.dragonfly.features.auth.presentation.login.LoginScreenRoot
import com.ralphmarondev.dragonfly.features.auth.presentation.register.RegisterScreenRoot
import com.ralphmarondev.dragonfly.features.dashboard.presentation.DashboardScreenRoot

@Composable
fun AppNavigation(
    startDestination: Routes = Routes.Login,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Routes.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(Routes.Dashboard) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegister = {
                    navController.navigate(Routes.Register) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Routes.Register> {
            RegisterScreenRoot(
                onRegisterSuccessful = {
                    navController.navigate(Routes.Dashboard) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLogin = {
                    navController.navigate(Routes.Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Routes.Dashboard> {
            DashboardScreenRoot(
                account = {
                    navController.navigate(Routes.Account) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Routes.Account> {
            AccountScreenRoot(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}