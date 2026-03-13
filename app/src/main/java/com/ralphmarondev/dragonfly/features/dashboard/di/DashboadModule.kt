package com.ralphmarondev.dragonfly.features.dashboard.di

import com.ralphmarondev.dragonfly.features.dashboard.presentation.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
}