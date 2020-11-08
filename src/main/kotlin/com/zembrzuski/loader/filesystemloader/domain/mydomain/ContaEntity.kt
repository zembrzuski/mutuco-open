package com.zembrzuski.loader.filesystemloader.domain.mydomain

/**
 * Conta que irá ser persistida no elasticsearch
 */
data class ContaEntity(

        val contaName: String,
        val trimester: Int,
        val year: Int,
        val value: Float,
        val codigoCvm: String,
        val valorDerivado: Boolean,
        val valorExato: Boolean

)