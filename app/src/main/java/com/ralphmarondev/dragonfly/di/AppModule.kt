package com.ralphmarondev.dragonfly.di

import com.ralphmarondev.dragonfly.core.di.coreModule
import com.ralphmarondev.dragonfly.features.auth.di.authModule
import com.ralphmarondev.dragonfly.features.dashboard.di.dashboardModule
import org.koin.dsl.module

val appModule = module {
    includes(coreModule)
    includes(authModule)
    includes(dashboardModule)
}