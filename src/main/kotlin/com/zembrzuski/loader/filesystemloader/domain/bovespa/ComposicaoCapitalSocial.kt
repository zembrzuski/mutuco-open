package com.zembrzuski.loader.filesystemloader.domain.bovespa

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("ArrayOfComposicaoCapitalSocialDemonstracaoFinanceira")
data class ComposicaoCapitalSocial(

        @set:JsonProperty("ComposicaoCapitalSocialDemonstracaoFinanceira")
        var composicaoCapital: List<ComposicaoCapital> = ArrayList()

)

@JsonRootName("ComposicaoCapitalSocialDemonstracaoFinanceira")
data class ComposicaoCapital(

        @set:JsonProperty("QuantidadeAcaoOrdinariaCapitalIntegralizado")
        var qtdAcaoOrdinaria: Long,

        @set:JsonProperty("QuantidadeAcaoPreferencialCapitalIntegralizado")
        var qtdAcaoPreferencial: Long,

        @set:JsonProperty("QuantidadeTotalAcaoCapitalIntegralizado")
        var qtdAcaoTotal: Long,

        @set:JsonProperty("QuantidadeAcaoOrdinariaTesouraria")
        var qtdAcaoOrdinariaTesouraria: Long,

        @set:JsonProperty("QuantidadeAcaoPreferencialTesouraria")
        var qtdAcaoPreferencialTesouraria: Long,

        @set:JsonProperty("QuantidadeTotalAcaoTesouraria")
        var qtdAcaoTotalTesouraria: Long

)