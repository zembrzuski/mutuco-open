package com.zembrzuski.loader.filesystemloader.domain.bovespa

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("Documento")
data class FormularioCadastral(

        @set:JsonProperty("CompanhiaAberta")
        var companhiaAberta: CompanhiaAberta,

        @set:JsonProperty("DataEntrega")
        var dataEntrega: String

)


data class CompanhiaAberta(

        @set:JsonProperty("CodigoCvm")
        var codigoCvm: String,

        @set:JsonProperty("NomeRazaoSocialCompanhiaAberta")
        var nomeEmpresa: String

)
