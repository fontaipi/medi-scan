package com.fontaipi.mediscan.repository

import com.fontaipi.mediscan.database.entity.DrugEntity
import kotlinx.coroutines.flow.Flow

interface DrugRepository {
    suspend fun loadFromNetwork()
    fun getByCisCode(cisCode: String): Flow<DrugEntity>
    fun searchByName(name: String, limit: Int): Flow<List<DrugEntity>>
}
