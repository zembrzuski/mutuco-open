package com.zembrzuski.loader.filesystemloader.domain.mydomain

/**
 * Guarda, para uma determinada conta, o valor da soma de cada um dos seus demonstrativos para um
 * determinado ano.
 */
data class AnnualSum(
        val conta: ContasEnum,
        val year: Int,
        val value: Float,
        val tipoDemonstrativo: TipoDemonstrativoEnum
)