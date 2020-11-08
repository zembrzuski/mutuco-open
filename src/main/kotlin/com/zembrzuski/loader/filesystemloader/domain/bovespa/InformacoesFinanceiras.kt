package com.zembrzuski.loader.filesystemloader.domain.bovespa

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("ArrayOfInfoFinaDFin")
data class InformacoesFinanceiras (

        @set:JsonProperty("InfoFinaDFin")
        var infoFinanceiras: List<InfoFinaDFin> = ArrayList()

)

@JsonRootName("InfoFinaDFin")
data class InfoFinaDFin(

        @set:JsonProperty("DescricaoConta1")
        var descricaoConta: String,

        @set:JsonProperty("ValorConta1")
        var valorConta1: Float,

        @set:JsonProperty("ValorConta2")
        var valorConta2: Float,

        @set:JsonProperty("ValorConta3")
        var valorConta3: Float,

        @set:JsonProperty("ValorConta4")
        var valorConta4: Float,

        @set:JsonProperty("ValorConta5")
        var valorConta5: Float,

        @set:JsonProperty("ValorConta6")
        var valorConta6: Float,

        @set:JsonProperty("PlanoConta")
        var planoConta: PlanoConta

)


@JsonRootName("PlanoConta")
data class PlanoConta(

        @set:JsonProperty("NumeroConta")
        var numeroConta: String

)