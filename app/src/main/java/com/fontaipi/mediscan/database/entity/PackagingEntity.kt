package com.fontaipi.mediscan.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

enum class CommunityAgreement {
    YES, NO, UNKNOWN
}

@Entity(
    tableName = "packaging",
    foreignKeys = [
        ForeignKey(
            entity = DrugEntity::class,
            parentColumns = ["cis_code"],
            childColumns = ["cis_code"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackagingEntity(
    @PrimaryKey
    @ColumnInfo(name = "cip_code7")
    val cipCode7: String,
    @ColumnInfo(name = "presentation_label")
    val presentationLabel: String,
    @ColumnInfo(name = "administrative_status")
    val administrativeStatus: String,
    @ColumnInfo(name = "marketing_state")
    val marketingState: String,
    @ColumnInfo(name = "marketing_declaration_date")
    val marketingDeclarationDate: String,
    @ColumnInfo(name = "cip_code13")
    val cipCode13: String,
    @ColumnInfo(name = "community_agreement")
    val communityAgreement: CommunityAgreement,
    @ColumnInfo(name = "refund_rate")
    val refundRate: String,
    @ColumnInfo(name = "price_excluding_dispensing_fee")
    val priceExcludingDispensingFee: BigDecimal?,
    @ColumnInfo(name = "dispensing_fee")
    val dispensingFee: BigDecimal?,
    @ColumnInfo(name = "price_including_fee")
    val priceIncludingFee: BigDecimal?,
    @ColumnInfo(name = "refund_conditions")
    val refundConditions: String?,
    @ColumnInfo(name = "cis_code")
    val cisCode: String
)
