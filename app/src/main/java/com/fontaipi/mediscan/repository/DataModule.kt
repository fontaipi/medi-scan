package com.fontaipi.mediscan.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsCisBdpmRepository(
        cisBdpmRepository: DrugRepositoryImpl
    ): DrugRepository

    @Binds
    fun bindsCisCipBdpmRepository(
        cisCipBdpmRepository: PackagingRepositoryImpl
    ): PackagingRepository
}
