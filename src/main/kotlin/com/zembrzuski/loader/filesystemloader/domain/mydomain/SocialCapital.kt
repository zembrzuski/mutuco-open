package com.zembrzuski.loader.filesystemloader.domain.mydomain

data class SocialCapital(
        val general: StockQuantity, // acoes totais
        val treasury: StockQuantity // acoes em tesouraria
)

data class StockQuantity(
        val ordinary: Long,
        val preferred: Long,
        val total: Long
)
