package com.fontaipi.mediscan.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fontaipi.mediscan.database.entity.DrugEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDao {
    @Query("SELECT * FROM drug")
    fun getAll(): List<DrugEntity>

    @Query("SELECT * FROM drug WHERE name LIKE '%' || (:name) || '%' LIMIT (:limit)")
    fun getEntitiesStreamByName(name: String, limit: Int): Flow<List<DrugEntity>>

    @Query("SELECT * FROM drug WHERE name LIKE '%' || (:name) || '%'")
    fun pagingSource(name: String): PagingSource<Int, DrugEntity>

    @Query("SELECT * FROM drug WHERE cis_code = (:cisCode)")
    fun getEntityStreamByCisCode(cisCode: String): Flow<DrugEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drug: DrugEntity): Long

    @Delete
    fun delete(drug: DrugEntity)
}
