package com.fontaipi.mediscan.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMediScanDatabase(
        @ApplicationContext context: Context
    ): MediScanDatabase {
        val builder = Room.databaseBuilder(
            context,
            MediScanDatabase::class.java,
            "mediscan-database"
        )
        // TODO remove debug LOG or use debug flag
        //        builder.setQueryCallback({ sqlQuery, bindArgs ->
        //            println("SQL Query: $sqlQuery SQL Args: $bindArgs")
        //        }, Executors.newSingleThreadExecutor())
        return builder.build()
    }
}
