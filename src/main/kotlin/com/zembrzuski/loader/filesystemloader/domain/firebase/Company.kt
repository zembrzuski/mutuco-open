package com.zembrzuski.loader.filesystemloader.domain.firebase

/**
 * Company que vai ser persistida no firebase
 */
data class Company(
        val cvmCode: String,
        val site: String,
        val mainActivity: String,
        val stockCodes: List<String>,
        val name: String,

        val section: String,
        val subsection: String,
        val segment: String,

        val lastBalanceId: Long,
        val lastBalanceDate: String
)
