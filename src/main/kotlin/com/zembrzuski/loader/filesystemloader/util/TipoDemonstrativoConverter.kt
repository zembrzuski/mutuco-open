package com.zembrzuski.loader.filesystemloader.util

import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import org.springframework.stereotype.Component

@Component
class TipoDemonstrativoConverter {

    // TODO essa classe pode ser refatorada, e muitas dessas funcoes podem ser somente uma funcao mais generica.

    fun fromXmls(xmls: Map<String, String>): TipoDemonstrativoEnum {
        return when {
            xmls.containsKey("dfp-InfoFinaDFin.xml") -> TipoDemonstrativoEnum.DFP
            xmls.containsKey("itr-InfoFinaDFin.xml") -> TipoDemonstrativoEnum.ITR
            else -> throw IllegalStateException("not dfp nor itr")
        }
    }

    fun getInfoFinaDFinKey(xmls: Map<String, String>): String {
        return when {
            xmls.containsKey("dfp-InfoFinaDFin.xml") -> "dfp-InfoFinaDFin.xml"
            xmls.containsKey("itr-InfoFinaDFin.xml") -> "itr-InfoFinaDFin.xml"
            else -> throw IllegalStateException("not dfp nor itr")
        }
    }

    fun getFormularioDemonstracaoFinanceiraKey(xmls: Map<String, String>): String {
        return when {
            xmls.containsKey("dfp-FormularioDemonstracaoFinanceiraDFP.xml") -> "dfp-FormularioDemonstracaoFinanceiraDFP.xml"
            xmls.containsKey("itr-FormularioDemonstracaoFinanceiraITR.xml") -> "itr-FormularioDemonstracaoFinanceiraITR.xml"
            else -> throw IllegalStateException("not dfp nor itr")
        }
    }

    fun getCapitalSocialKey(xmls: Map<String, String>): String {
        return when {
            xmls.containsKey("dfp-ComposicaoCapitalSocialDemonstracaoFinanceiraNegocios.xml") -> "dfp-ComposicaoCapitalSocialDemonstracaoFinanceiraNegocios.xml"
            xmls.containsKey("itr-ComposicaoCapitalSocialDemonstracaoFinanceiraNegocios.xml") -> "itr-ComposicaoCapitalSocialDemonstracaoFinanceiraNegocios.xml"
            else -> throw IllegalStateException("not dfp nor itr")
        }
    }

}