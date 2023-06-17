package com.fontaipi.mediscan.domain

import com.fontaipi.mediscan.database.entity.BdmStatus
import com.fontaipi.mediscan.database.entity.DrugEntity
import com.fontaipi.mediscan.database.entity.PackagingEntity
import java.math.BigDecimal

data class DrugDetails(
    val cisCode: String,
    val name: String,
    val bdmStatus: BdmStatus?,
    val presentations: List<Presentation>
) {
    constructor(drug: DrugEntity, presentations: List<PackagingEntity>) : this(
        cisCode = drug.cisCode,
        name = drug.name,
        bdmStatus = drug.bdmStatus,
        presentations = presentations.map { Presentation(it) }
    )
}

data class Presentation(
    val cipCode: String,
    val label: String,
    val marketingDeclarationDate: String,
    val refundRate: String,
    val priceExcludingDispensingFee: BigDecimal?,
    val dispensingFee: BigDecimal?,
    val priceIncludingFee: BigDecimal?
) {
    constructor(presentation: PackagingEntity) : this(
        cipCode = presentation.cipCode13,
        label = presentation.presentationLabel.replaceFirstChar(Char::titlecase),
        marketingDeclarationDate = presentation.marketingDeclarationDate,
        refundRate = presentation.refundRate,
        priceExcludingDispensingFee = presentation.priceExcludingDispensingFee,
        dispensingFee = presentation.dispensingFee,
        priceIncludingFee = presentation.priceIncludingFee
    )
}

val samplePresentation = Presentation(
    cipCode = "34000935955838",
    label = "Plaquette(s) thermoformée(s) PVC-aluminium de 8 comprimé(s)",
    marketingDeclarationDate = "02/01/2003",
    refundRate = "65%",
    priceExcludingDispensingFee = BigDecimal("1.07"),
    dispensingFee = BigDecimal("1.02"),
    priceIncludingFee = BigDecimal("2.09")
)
