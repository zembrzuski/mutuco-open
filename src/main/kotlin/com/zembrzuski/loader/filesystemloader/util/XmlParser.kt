package com.zembrzuski.loader.filesystemloader.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapitalSocial
import com.zembrzuski.loader.filesystemloader.domain.bovespa.FormularioCadastral
import com.zembrzuski.loader.filesystemloader.domain.bovespa.FormularioDemonstracaoFinanceira
import com.zembrzuski.loader.filesystemloader.domain.bovespa.InformacoesFinanceiras
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class XmlParser {

    internal val kotlinXmlMapper =
            XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) })
                    .registerKotlinModule()
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun parseFormularioCadastral(get: String): FormularioCadastral {
        return kotlinXmlMapper.readValue(get)
    }

    fun parseInfoFinaDFin(infoFinaDFinKey: String): InformacoesFinanceiras {
        return kotlinXmlMapper.readValue(infoFinaDFinKey)
    }

    fun parseFormularioDemonstracaoFinanceira(formulario: String): FormularioDemonstracaoFinanceira {
        return kotlinXmlMapper.readValue(formulario)
    }

    fun parseComposicaoCapitalSocial(formulario: String): ComposicaoCapitalSocial {
        return kotlinXmlMapper.readValue(formulario)
    }

    fun getKey(key: String, xmls: Map<String, String>): String {
        return xmls[key] ?: throw IllegalStateException("could not find key $key in xml map")
    }

}