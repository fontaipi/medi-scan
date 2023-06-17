package com.fontaipi.mediscan.domain

import com.fontaipi.mediscan.repository.DrugRepository
import com.fontaipi.mediscan.repository.PackagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDrugDetails @Inject constructor(
    private val drugRepository: DrugRepository,
    private val packagingRepository: PackagingRepository
) {
    operator fun invoke(cisCode: String): Flow<DrugDetails?> {
        return combine(
            drugRepository.getByCisCode(cisCode),
            packagingRepository.getPresentationsByCipCode(cisCode),
            ::Pair
        ).map { (drug, presentations) ->
            DrugDetails(drug, presentations)
        }
    }
}
