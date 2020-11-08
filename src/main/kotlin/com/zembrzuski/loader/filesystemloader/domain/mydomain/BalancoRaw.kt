package com.zembrzuski.loader.filesystemloader.domain.mydomain

import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapital
import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin

data class BalancoRaw(
        val codigoCvm: String,
        val nomeEmpresa: String,
        val dataReferenciaDocumento: String,
        val tipoDemonstrativoEnum: TipoDemonstrativoEnum,
        val infoFinanceiras: List<InfoFinaDFin>,
        val numeroDocumento: Long,
        val capitalSocial: ComposicaoCapital,
        val escalaMoeda: Int,
        val escalaQuantidade: Int,
        val presentationDate: String
)
