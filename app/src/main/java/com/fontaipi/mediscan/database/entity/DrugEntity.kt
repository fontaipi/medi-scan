package com.fontaipi.mediscan.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class BdmStatus {
    ALERT,
    AVAILABILITY_WARNING
}

@Entity(tableName = "drug")
data class DrugEntity(
    @PrimaryKey
    @ColumnInfo(name = "cis_code")
    val cisCode: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "pharmaceutical_form")
    val pharmaceuticalForm: String,
    @ColumnInfo(name = "administration_routes")
    val administrationRoutes: String,
    @ColumnInfo(name = "authorization_status")
    val authorizationStatus: String,
    @ColumnInfo(name = "authorization_type")
    val authorizationType: String,
    @ColumnInfo(name = "marketing_status")
    val marketingStatus: String,
    @ColumnInfo(name = "market_introduction_date")
    val marketIntroductionDate: String,
    @ColumnInfo(name = "bdm_status")
    val bdmStatus: BdmStatus?,
    @ColumnInfo(name = "european_authorization_number")
    val europeanAuthorizationNumber: String?,
    @ColumnInfo(name = "holder")
    val holder: String,
    @ColumnInfo(name = "enhanced_monitoring")
    val enhancedMonitoring: Boolean
)
