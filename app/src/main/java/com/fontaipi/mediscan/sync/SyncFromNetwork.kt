package com.fontaipi.mediscan.sync

import com.fontaipi.mediscan.repository.DrugRepository
import com.fontaipi.mediscan.repository.PackagingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncFromNetwork @Inject constructor(
    private val drugRepository: DrugRepository,
    private val packagingRepository: PackagingRepository

) {
    suspend fun sync() {
        drugRepository.loadFromNetwork()
        packagingRepository.loadFromNetwork()
    }
}
