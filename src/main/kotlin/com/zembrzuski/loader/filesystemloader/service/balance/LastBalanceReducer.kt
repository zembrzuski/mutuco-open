package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.time.LocalDate

/**
 * Pega o último balanco publicado para cada trimestre.
 *
 * Isso é importante para pegar as informações mais confiáveis: imagine,
 * por exemplo, que um balanço foi publicado com erro e depois foi republicado.
 * Essa classe trata de pegar o balanço que foi republicado.
 */
@Component
class LastBalanceReducer {

    fun reduceBalanceById(
            balanceTrimesterInformation: List<BalanceTrimesterInformation>)
            : BalanceTrimesterInformation {

        return balanceTrimesterInformation
                .maxBy { it.year * 4 + it.trimester } ?: throw IllegalStateException("could not find last balance")
    }

    fun reduceLastBalanceForEachTrimester(balances: List<Balanco>): List<Balanco> {
        return balances
                .groupBy { balance -> balance.dataReferenciaDocumento }
                .mapValues { map -> getLastBalanceForCurrentTrimester(map) }
                .values
                .toList()
    }

    private fun getLastBalanceForCurrentTrimester(map: Map.Entry<LocalDate, List<Balanco>>): Balanco {
        return map.value.reduce { a, b ->
            if (a.numeroDocumento > b.numeroDocumento) a else b
        }
    }

}