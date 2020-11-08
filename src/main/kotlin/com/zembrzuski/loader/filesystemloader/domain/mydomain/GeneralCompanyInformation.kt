package com.zembrzuski.loader.filesystemloader.domain.mydomain

import com.zembrzuski.loader.filesystemloader.domain.firebase.Company

data class GeneralCompanyInformation(
        val cvmCode: String,
        val sector: SectorClassification,
        val site: String,
        val mainActivity: String,
        val stockCodes: List<String>,
        val negotiationName: String,
        val lastBalanceId: Long? = null,
        val lastBalanceDate: String? = null
)

data class SectorClassification(
        val section: String,
        val subsection: String,
        val segment: String
)

fun GeneralCompanyInformation.toFirebaseCompany(): Company {
    val lastBalanceIdToPersist = lastBalanceId ?: throw IllegalStateException("could not fild last balance id")
    val date = lastBalanceDate ?: throw java.lang.IllegalStateException("could not find balance date")

    return Company(cvmCode.padStart(6, '0'), site, mainActivity, stockCodes,
            negotiationName, sector.section, sector.subsection, sector.segment, lastBalanceIdToPersist, date)
}
