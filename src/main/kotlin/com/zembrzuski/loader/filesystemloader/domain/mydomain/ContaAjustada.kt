package com.zembrzuski.loader.filesystemloader.domain.mydomain

/**
 * Conta é uma informação financeira de uma empresa.
 *
 * Por exemplo, a empresa X lançou um balanco com N contas. Exemplo de conta: lucro liquido, patrimonio liquido.
 *
 * É a informação ajustada confome minha vontade.
 */
data class ContaAjustada(

        val conta: ContasEnum,
        val trimester: Int,
        val year: Int,
        val value: Float,
        val codigoCvm: String,

        // guarda se o valor eh exatamente como divulgado pela bovespa ou se foi inferido a partir de outros valores
        val valorDerivado: Boolean,

        // guarda se o valor eh exato ou se contem alguma imprecisao feita por alguma inferencia.
        val valorExato: Boolean,

        val balanceId: Long
)
