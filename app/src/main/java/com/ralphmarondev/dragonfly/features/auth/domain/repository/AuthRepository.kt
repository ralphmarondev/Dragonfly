package com.ralphmarondev.dragonfly.features.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(
        displayName: String,
        vehicleId: String,
        email: String, password: String
    ): Boolean
}