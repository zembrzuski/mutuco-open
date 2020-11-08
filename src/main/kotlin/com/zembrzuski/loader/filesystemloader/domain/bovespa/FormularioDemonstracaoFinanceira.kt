package com.zembrzuski.loader.filesystemloader.domain.bovespa

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("FormularioDemonstracaoFinanceira")
data class FormularioDemonstracaoFinanceira(

        @set:JsonProperty("Documento")
        var documento: Documento

)

@JsonRootName("Documento")
data class Documento(

        @set:JsonProperty("DataReferenciaDocumento")
        var dataReferenciaDocumento: String,

        @set:JsonProperty("CodigoEscalaMoeda")
        var codigoEscalaMoeda: Int,

        @set:JsonProperty("CodigoEscalaQuantidade")
        var codigoEscalaQuantidade: Int

)
