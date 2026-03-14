package com.ralphmarondev.dragonfly.features.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import com.ralphmarondev.dragonfly.core.domain.model.Result
import com.ralphmarondev.dragonfly.core.domain.model.User
import com.ralphmarondev.dragonfly.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preferences: AppPreferences
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: return false

        val doc = firestore.collection("users")
            .document(user.uid)
            .get()
            .await()
        val vehicleId = doc.getString("vehicleId") ?: return false

        preferences.setAuthenticated(true)
        preferences.setEmail(email)
        preferences.setVehicleId(vehicleId)
        return true
    }

    override suspend fun register(
        displayName: String,
        vehicleId: String,
        email: String,
        password: String
    ): Boolean {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: return false

        firestore.collection("users")
            .document(user.uid)
            .set(
                mapOf(
                    "uid" to user.uid,
                    "email" to user.email,
                    "displayName" to displayName,
                    "vehicleId" to vehicleId
                )
            )
            .await()

        preferences.setAuthenticated(true)
        preferences.setEmail(email)
        preferences.setVehicleId(vehicleId)
        return true
    }

    override suspend fun getAccountInformation(): Result<User> {
        val savedEmail = preferences.getEmail().first()
        val savedPassword = preferences.getPassword().first()

        val authResult = auth.signInWithEmailAndPassword(savedEmail, savedPassword).await()
        val firebaseUser = authResult.user ?: return Result.Error(message = "Invalid credentials.")

        val doc = firestore.collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()
        val vehicleId = doc.getString("vehicleId") ?: ""
        val displayName = doc.getString("displayName") ?: ""
        val email = doc.getString("email") ?: ""

        val user = User(
            displayName = displayName,
            vehicleId = vehicleId,
            email = email
        )
        return Result.Success(user)
    }
}