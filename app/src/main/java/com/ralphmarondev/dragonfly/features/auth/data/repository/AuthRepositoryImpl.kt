package com.ralphmarondev.dragonfly.features.auth.data.repository

import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return email == "ralphmaron@cute.com" && password == "super.cute"
    }

    override suspend fun register(email: String, password: String): Boolean {
        return true
    }
}