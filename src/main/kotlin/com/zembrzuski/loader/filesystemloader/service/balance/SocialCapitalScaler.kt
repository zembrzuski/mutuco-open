package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Scale
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SocialCapital
import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity
import org.springframework.stereotype.Component

@Component
class SocialCapitalScaler {

    fun scale(balance: Balanco): Balanco {
        val socialCapital = balance.socialCapital

        val scaledSocialCapital = socialCapital.copy(
                general = scale(socialCapital.general, balance.quantityScale),
                treasury = scale(socialCapital.treasury, balance.quantityScale))

        return balance.copy(socialCapital = scaledSocialCapital)
    }

    private fun scale(inp: StockQuantity, quantityScale: Scale): StockQuantity {
        return StockQuantity(
                inp.ordinary * quantityScale.multiplicationFactor.toLong(),
                inp.preferred * quantityScale.multiplicationFactor.toLong(),
                inp.total * quantityScale.multiplicationFactor.toLong())
    }

}
