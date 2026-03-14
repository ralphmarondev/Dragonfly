package com.ralphmarondev.dragonfly.features.auth.domain.repository

import com.ralphmarondev.dragonfly.core.domain.model.Result
import com.ralphmarondev.dragonfly.core.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean

    suspend fun register(
        displayName: String,
        vehicleId: String,
        email: String,
        password: String
    ): Boolean

    suspend fun getAccountInformation(): Result<User>
}