package com.fontaipi.mediscan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fontaipi.mediscan.database.entity.PackagingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackagingDao {
    @Query("SELECT * FROM packaging WHERE cip_code13 = (:cipCode)")
    fun getEntityStreamByCipCode(cipCode: String): Flow<PackagingEntity?>

    @Query("SELECT * FROM packaging WHERE cis_code = (:cisCode)")
    fun getEntitiesStreamByCisCode(cisCode: String): Flow<List<PackagingEntity>>

    @Query("SELECT cis_code FROM packaging WHERE cip_code13 = (:cipCode)")
    fun getCisCodeStreamByCipCode(cipCode: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(packagingEntity: PackagingEntity): Long

    @Delete
    fun delete(packagingEntity: PackagingEntity)
}
