package com.ralphmarondev.dragonfly.core.worker

import android.content.Context
import android.location.Location
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.ralphmarondev.dragonfly.core.common.NotificationHelper
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class LocationCheckWorker(
    context: Context,
    params: WorkerParameters,
    private val prefs: AppPreferences,
    private val firestore: FirebaseFirestore
) : CoroutineWorker(context, params) {

    companion object {
        private const val TRIGGER_IF_MOVED_METERS = 10f
    }

    override suspend fun doWork(): Result {
        return try {
            val vehicleId = prefs.getVehicleId().first()
            if (vehicleId.isEmpty()) return Result.success()

            val savedLocation = prefs.getLocation().first()
            val snapshot = firestore.collection("vehicles")
                .document(vehicleId)
                .get()
                .await()

            val lat = snapshot.getDouble("latitude")
            val lng = snapshot.getDouble("longitude")

            if (lat != null && lng != null) {
                val hasChanged = savedLocation == null ||
                        hasSignificantChange(
                            savedLocation.latitude,
                            savedLocation.longitude,
                            lat,
                            lng
                        )

                if (hasChanged) {
                    prefs.setLocation(lat, lng)
                    NotificationHelper.sendNotification(
                        context = applicationContext,
                        id = 1001,
                        title = "Vehicle Moved",
                        content = "New location: $lat, $lng",
                        autoCancel = true
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun hasSignificantChange(
        oldLat: Double,
        oldLng: Double,
        newLat: Double,
        newLng: Double
    ): Boolean {
        val results = FloatArray(1)

        Location.distanceBetween(
            oldLat, oldLng,
            newLat, newLng,
            results
        )

        return results[0] > TRIGGER_IF_MOVED_METERS
    }
}