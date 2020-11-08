package com.zembrzuski.loader.filesystemloader.domain.firebase

data class BalanceTrimesterInformation(
        val balanceId: Long,
        val cvmCode: String,
        val year: Int,
        val trimester: Int,
        val netProfit: Float? = null, // lucro liquido
        val netWealth: Float? = null, // patrimonio liquido
        val socialCapitalEntity: SocialCapitalEntity? = null
)
