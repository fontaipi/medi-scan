package com.fontaipi.mediscan.domain

import com.fontaipi.mediscan.database.entity.DrugEntity

data class DrugSearch(
    val name: String,
    val cisCode: String
) {
    constructor(drug: DrugEntity) : this(
        name = drug.name,
        cisCode = drug.cisCode
    )
}
