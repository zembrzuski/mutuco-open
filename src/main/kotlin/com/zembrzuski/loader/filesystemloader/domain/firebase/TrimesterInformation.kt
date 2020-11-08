package com.zembrzuski.loader.filesystemloader.domain.firebase

data class TrimesterInformation(
        val balanceTrimesterInformation: BalanceTrimesterInformation,
        val quoteRealPrice: List<Quote> = emptyList(),
        val quoteAdjustedPrice: List<Quote> = emptyList(),
        val indicators: List<Indicator> = emptyList()
)

data class Indicator(
        val name: String,
        val value: Float
)