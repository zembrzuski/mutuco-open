package com.zembrzuski.loader.filesystemloader.domain.mydomain

import java.time.LocalDate

/**
 * Contém dados extraídos de um balanco publicado pela Bovespa.
 */
data class Balanco(
        val codigoCvm: String,
        val nomeEmpresa: String,
        val dataReferenciaDocumento: LocalDate,
        val tipoDemonstrativoEnum: TipoDemonstrativoEnum,
        val numeroDocumento: Long,
        val planoContas: List<Conta>,
        val socialCapital: SocialCapital,
        val valueScale: Scale,
        val quantityScale: Scale
)

