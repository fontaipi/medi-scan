package com.fontaipi.mediscan.database

import androidx.room.TypeConverter
import com.fontaipi.mediscan.database.entity.BdmStatus
import com.fontaipi.mediscan.database.entity.CommunityAgreement
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromBdmStatus(value: BdmStatus?): Int? {
        return value?.ordinal
    }

    @TypeConverter
    fun toBdmStatus(value: Int?): BdmStatus? {
        return value?.let { BdmStatus.values().getOrNull(it) }
    }

    @TypeConverter
    fun fromCommunityAgreement(agreement: CommunityAgreement): String {
        return when (agreement) {
            CommunityAgreement.YES -> "oui"
            CommunityAgreement.NO -> "non"
            CommunityAgreement.UNKNOWN -> "inconnu"
        }
    }

    @TypeConverter
    fun toCommunityAgreement(agreement: String): CommunityAgreement {
        return when {
            agreement.equals("oui", ignoreCase = true) -> CommunityAgreement.YES
            agreement.equals("non", ignoreCase = true) -> CommunityAgreement.NO
            else -> CommunityAgreement.UNKNOWN
        }
    }

    @TypeConverter
    fun bigDecimalToDouble(input: BigDecimal?): Double? {
        return input?.toDouble()
    }

    @TypeConverter
    fun doubleToBigDecimal(input: Double?): BigDecimal? {
        if (input == null) return null
        return BigDecimal.valueOf(input) ?: BigDecimal.ZERO
    }
}
