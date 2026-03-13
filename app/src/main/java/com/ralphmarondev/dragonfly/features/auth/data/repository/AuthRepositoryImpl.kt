package com.ralphmarondev.dragonfly.features.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val preferences: AppPreferences
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        authResult.user ?: return false

        preferences.setAuthenticated(true)
        preferences.setEmail(email)
        return true
    }

    override suspend fun register(email: String, password: String): Boolean {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        authResult.user ?: return false

        preferences.setAuthenticated(true)
        preferences.setEmail(email)
        return true
    }
}