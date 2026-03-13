package com.ralphmarondev.dragonfly.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single { AppPreferences(context = androidContext().applicationContext) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}