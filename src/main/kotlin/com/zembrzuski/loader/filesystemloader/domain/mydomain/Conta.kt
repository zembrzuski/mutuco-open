package com.zembrzuski.loader.filesystemloader.domain.mydomain

/**
 * Conta é uma informação financeira de uma empresa.
 *
 * Por exemplo, a empresa X lançou um balanco com N contas. Exemplo de conta: lucro liquido, patrimonio liquido.
 *
 * É a informação crua, tal como ela foi extraída da bovespa.
 */
data class Conta (
    val conta: ContasEnum,
    val trimester: Int,
    val year: Int,
    val value: Float,
    val numeroDocumento: Long,
    val tipoDemonstrativo: TipoDemonstrativoEnum
)