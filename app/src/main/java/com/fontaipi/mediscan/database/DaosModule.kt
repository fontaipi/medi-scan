package com.fontaipi.mediscan.database

import com.fontaipi.mediscan.database.dao.DrugDao
import com.fontaipi.mediscan.database.dao.PackagingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesCisBdpmDao(
        database: MediScanDatabase
    ): DrugDao = database.cisBdpmDao()

    @Provides
    fun providesCisCipBdpmDao(
        database: MediScanDatabase
    ): PackagingDao = database.cisCipBdpmDao()
}
