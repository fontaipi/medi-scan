package com.fontaipi.mediscan.repository

import com.fontaipi.mediscan.database.entity.PackagingEntity
import kotlinx.coroutines.flow.Flow

interface PackagingRepository {
    suspend fun loadFromNetwork()
    fun getByCipCode(cipCode: String): Flow<PackagingEntity?>
    suspend fun getCisCodeByCipCode(cipCode: String): String?
    fun getPresentationsByCipCode(cisCode: String): Flow<List<PackagingEntity>>
}
