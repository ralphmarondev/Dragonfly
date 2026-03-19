package com.ralphmarondev.dragonfly.core.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object LocationWorkerScheduler {

    private const val WORK_NAME = "location_check_work"

    fun schedule(context: Context, demo: Boolean = true) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workManager = WorkManager.getInstance(context)

        if (demo) {
            val request = OneTimeWorkRequestBuilder<LocationCheckWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10, // seconds before retry on failure
                    TimeUnit.SECONDS
                )
                .build()

            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
        } else {
            val periodicRequest = PeriodicWorkRequestBuilder<LocationCheckWorker>(
                15,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicRequest
            )
        }
    }
}