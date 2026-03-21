package com.ralphmarondev.dragonfly.features.dashboard.di

import com.ralphmarondev.dragonfly.features.dashboard.data.repository.DashboardRepositoryImpl
import com.ralphmarondev.dragonfly.features.dashboard.domain.repository.DashboardRepository
import com.ralphmarondev.dragonfly.features.dashboard.presentation.DashboardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {
    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
    viewModelOf(::DashboardViewModel)
}