package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapital
import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapitalSocial
import org.springframework.stereotype.Component

/**
 * Valida e recupera o capital social de uma empresa.
 *
 * Ainda não tenho confiança de que essa lógica realmente está boa.
 */
@Component
class CapitalSocialRetriever {

    fun getCapitalSocial(capitalSocial: ComposicaoCapitalSocial): ComposicaoCapital {
        return capitalSocial.composicaoCapital
                .filter { !(it.qtdAcaoOrdinaria == 0L && it.qtdAcaoPreferencial == 0L && it.qtdAcaoOrdinariaTesouraria == 0L) }
                .distinct()
                .last()
    }

}
