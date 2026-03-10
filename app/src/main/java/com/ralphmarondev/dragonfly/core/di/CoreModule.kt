package com.ralphmarondev.dragonfly.core.di

import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single { AppPreferences(context = androidContext().applicationContext) }
}