package com.fontaipi.mediscan.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fontaipi.mediscan.database.dao.DrugDao
import com.fontaipi.mediscan.database.dao.PackagingDao
import com.fontaipi.mediscan.database.entity.DrugEntity
import com.fontaipi.mediscan.database.entity.PackagingEntity

@Database(
    entities = [
        DrugEntity::class,
        PackagingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MediScanDatabase : RoomDatabase() {
    abstract fun cisBdpmDao(): DrugDao
    abstract fun cisCipBdpmDao(): PackagingDao
}
