package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalancoRaw
import com.zembrzuski.loader.filesystemloader.service.SocialCapitalConverter
import com.zembrzuski.loader.filesystemloader.service.planocontas.PlanoContasConverter
import com.zembrzuski.loader.filesystemloader.service.planocontas.ScaleConverter
import com.zembrzuski.loader.filesystemloader.util.DateParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BalanceConverter @Autowired constructor(
        val dateParser: DateParser,
        val planoContasConverter: PlanoContasConverter,
        val socialCapitalConverter: SocialCapitalConverter,
        val scaleConverter: ScaleConverter) {

    fun convert(raw: BalancoRaw): Balanco {
        val documentDate = dateParser.parse(raw.dataReferenciaDocumento)
        val allAccounts = planoContasConverter.convert(raw, documentDate)
        val socialCapital = socialCapitalConverter.convertSocialCapital(raw.capitalSocial, documentDate)
        val valuesScale = scaleConverter.convert(raw.escalaMoeda)
        val quantityScale = scaleConverter.convert(raw.escalaQuantidade)

        return Balanco(
                raw.codigoCvm.replace("-", "").trim(),
                raw.nomeEmpresa.trim(),
                documentDate,
                raw.tipoDemonstrativoEnum,
                raw.numeroDocumento,
                allAccounts,
                socialCapital,
                valuesScale,
                quantityScale)
    }

}
