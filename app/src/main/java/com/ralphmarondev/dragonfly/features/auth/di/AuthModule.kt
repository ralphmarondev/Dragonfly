package com.ralphmarondev.dragonfly.features.auth.di

import com.ralphmarondev.dragonfly.features.auth.data.repository.AuthRepositoryImpl
import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository
import com.ralphmarondev.dragonfly.features.auth.presentation.login.LoginViewModel
import com.ralphmarondev.dragonfly.features.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}