package com.fontaipi.mediscan.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.fontaipi.mediscan.database.dao.PackagingDao
import com.fontaipi.mediscan.database.entity.CommunityAgreement
import com.fontaipi.mediscan.database.entity.PackagingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.URL
import javax.inject.Inject

class PackagingRepositoryImpl @Inject constructor(
    private val packagingDao: PackagingDao
) : PackagingRepository {
    override suspend fun loadFromNetwork(): Unit = withContext(Dispatchers.IO) {
        val url =
            URL("https://base-donnees-publique.medicaments.gouv.fr/telechargement.php?fichier=CIS_CIP_bdpm.txt")
        val stream = url.openStream()
        val reader = BufferedReader(InputStreamReader(stream, "windows-1252"))
        var line: String? = reader.readLine()
        while (line != null) {
            val tokens = line.split("\t")
            val communityAgreement = when {
                tokens[7].equals("oui", ignoreCase = true) -> CommunityAgreement.YES
                tokens[7].equals("non", ignoreCase = true) -> CommunityAgreement.NO
                else -> CommunityAgreement.UNKNOWN
            }
            val entity = PackagingEntity(
                cisCode = tokens[0],
                cipCode7 = tokens[1],
                presentationLabel = tokens[2],
                administrativeStatus = tokens[3],
                marketingState = tokens[4],
                marketingDeclarationDate = tokens[5],
                cipCode13 = tokens[6],
                communityAgreement = communityAgreement,
                refundRate = tokens[8],
                priceExcludingDispensingFee = stringToBigDecimal(tokens[9]),
                dispensingFee = stringToBigDecimal(tokens[9]),
                priceIncludingFee = stringToBigDecimal(tokens[9]),
                refundConditions = if (tokens.size > 12) tokens[12].replace("<br>", "\n") else null
            )
            try {
                packagingDao.insert(entity)
            } catch (e: SQLiteConstraintException) {
                Log.d("PackagingRepository", "Failed to INSERT line : $line")
            }
            line = reader.readLine()
        }
    }

    override fun getByCipCode(cipCode: String): Flow<PackagingEntity?> =
        packagingDao.getEntityStreamByCipCode(cipCode)

    override suspend fun getCisCodeByCipCode(cipCode: String): String? =
        withContext(Dispatchers.IO) {
            packagingDao.getCisCodeStreamByCipCode(cipCode)
        }

    override fun getPresentationsByCipCode(cisCode: String): Flow<List<PackagingEntity>> =
        packagingDao.getEntitiesStreamByCisCode(cisCode)
}

private fun stringToBigDecimal(input: String?): BigDecimal? {
    if (input.isNullOrBlank()) return null
    return try {
        BigDecimal(input.replace(",", "."))
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }
}
