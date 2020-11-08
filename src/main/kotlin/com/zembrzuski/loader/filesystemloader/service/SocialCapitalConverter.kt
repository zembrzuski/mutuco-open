package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapital
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SocialCapital
import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SocialCapitalConverter {

    fun convertSocialCapital(capitalSocial: ComposicaoCapital, documentDate: LocalDate): SocialCapital {
        val general = StockQuantity(
                capitalSocial.qtdAcaoOrdinaria,
                capitalSocial.qtdAcaoPreferencial,
                capitalSocial.qtdAcaoTotal)

        val treasury = StockQuantity(
                capitalSocial.qtdAcaoOrdinariaTesouraria,
                capitalSocial.qtdAcaoPreferencialTesouraria,
                capitalSocial.qtdAcaoTotalTesouraria)

        return SocialCapital(general, treasury)
    }

}