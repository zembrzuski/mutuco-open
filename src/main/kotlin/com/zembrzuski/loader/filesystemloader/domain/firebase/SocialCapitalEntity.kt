package com.zembrzuski.loader.filesystemloader.domain.firebase

import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity

data class SocialCapitalEntity(
        val general: StockQuantity, // acoes totais
        val treasury: StockQuantity // acoes em tesouraria
)
