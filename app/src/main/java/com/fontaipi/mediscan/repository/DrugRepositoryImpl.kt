package com.fontaipi.mediscan.repository

import com.fontaipi.mediscan.database.dao.DrugDao
import com.fontaipi.mediscan.database.entity.BdmStatus
import com.fontaipi.mediscan.database.entity.DrugEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject

class DrugRepositoryImpl @Inject constructor(
    private val drugDao: DrugDao
) : DrugRepository {
    override suspend fun loadFromNetwork(): Unit = withContext(Dispatchers.IO) {
        val url =
            URL("https://base-donnees-publique.medicaments.gouv.fr/telechargement.php?fichier=CIS_bdpm.txt")
        val stream = url.openStream()
        val reader = BufferedReader(InputStreamReader(stream, "windows-1252"))
        var line: String? = reader.readLine()
        while (line != null) {
            val columns = line.split("\t")
            val bdmStatus: BdmStatus? = when (columns.getOrNull(8)?.lowercase()) {
                "alerte" -> BdmStatus.ALERT
                "warning disponibilité" -> BdmStatus.AVAILABILITY_WARNING
                else -> null
            }
            val drugEntity = DrugEntity(
                cisCode = columns[0],
                name = columns[1],
                pharmaceuticalForm = columns[2],
                administrationRoutes = columns[3],
                authorizationStatus = columns[4],
                authorizationType = columns[5],
                marketingStatus = columns[6],
                marketIntroductionDate = columns[7],
                bdmStatus = bdmStatus, // null if absent
                europeanAuthorizationNumber = columns.getOrNull(9), // null if absent
                holder = columns[10],
                enhancedMonitoring = columns[11].equals(
                    "Oui",
                    ignoreCase = true
                ) // true if "Oui", false otherwise
            )
            drugDao.insert(drugEntity)
            line = reader.readLine()
        }
        reader.close()
    }

    override fun getByCisCode(cisCode: String): Flow<DrugEntity> =
        drugDao.getEntityStreamByCisCode(cisCode)

    override fun searchByName(name: String, limit: Int): Flow<List<DrugEntity>> =
        drugDao.getEntitiesStreamByName(name, limit)
}
