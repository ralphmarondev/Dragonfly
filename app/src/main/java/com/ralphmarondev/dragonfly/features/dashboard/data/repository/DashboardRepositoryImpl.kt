package com.ralphmarondev.dragonfly.features.dashboard.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import com.ralphmarondev.dragonfly.core.domain.model.Location
import com.ralphmarondev.dragonfly.features.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class DashboardRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val prefs: AppPreferences
) : DashboardRepository {

    companion object {
        private const val MAX_LOCATIONS = 50
    }

    override suspend fun getLastLocations(): List<Location> {
        val vehicleId = prefs.getVehicleId().first()
        if (vehicleId.isEmpty()) return emptyList()

        val snapshot = firestore.collection("locations")
            .document(vehicleId)
            .collection("location")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(MAX_LOCATIONS.toLong())
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val lat = doc.getDouble("latitude")
            val lng = doc.getDouble("longitude")
            val id = doc.getLong("timestamp") ?: System.currentTimeMillis()
            if (lat != null && lng != null) Location(id = id, latitude = lat, longitude = lng)
            else null
        }
    }
}