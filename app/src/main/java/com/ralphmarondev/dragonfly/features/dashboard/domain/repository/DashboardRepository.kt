package com.ralphmarondev.dragonfly.features.dashboard.domain.repository

import com.ralphmarondev.dragonfly.core.domain.model.Location

interface DashboardRepository {
    suspend fun getLastLocations(): List<Location>
}