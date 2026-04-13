package com.ralphmarondev.dragonfly.core.worker

import android.content.Context
import android.location.Location
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.ralphmarondev.dragonfly.core.common.NotificationHelper
import com.ralphmarondev.dragonfly.core.data.local.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class LocationCheckWorker(
    context: Context,
    params: WorkerParameters,
    private val prefs: AppPreferences,
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase
) : CoroutineWorker(context, params) {

    companion object {
        private const val TRIGGER_IF_MOVED_METERS = 10f
    }

    override suspend fun doWork(): Result {
        return try {
            val vehicleId = prefs.getVehicleId().first()
            if (vehicleId.isEmpty()) return Result.success()
            val db = database.reference

            val savedLocation = prefs.getLocation().first()
            val snapshot = db.child("HELLO123")
                .get()
                .await()

            val latestEntry = snapshot.children.maxByOrNull { it?.key?.toLongOrNull() ?: 0L }
            val lat = latestEntry?.child("latitude")?.getValue(Double::class.java)
            val lng = latestEntry?.child("longitude")?.getValue(Double::class.java)

            if (lat != null && lng != null) {
                if (savedLocation == null) {
                    prefs.setLocation(lat, lng)
                    NotificationHelper.sendNotification(
                        context = applicationContext,
                        title = "Vehicle Moved",
                        content = "Current Location: $lat, $lng",
                        autoCancel = true
                    )
                } else {
                    val hasChanged = hasSignificantChange(
                        savedLocation.latitude,
                        savedLocation.longitude,
                        lat,
                        lng
                    )

                    if (hasChanged) {
                        prefs.setLocation(lat, lng)
                        NotificationHelper.sendNotification(
                            context = applicationContext,
                            title = "Vehicle Moved",
                            content = "New location: $lat, $lng",
                            autoCancel = true
                        )
                    }
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