package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.SocialCapitalEntity
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContaAjustada
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SocialCapital
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class TrimesterInformationConverter {

    fun convert(cvmCode: String, contasAjustadas: List<ContaAjustada>, balances: List<Balanco>)
            : List<BalanceTrimesterInformation> {

        val lucroLiquido = getContaByTrimester(contasAjustadas, ContasEnum.LUCRO_LIQUIDO)
        val patrimonioLiquido = getContaByTrimester(contasAjustadas, ContasEnum.PATRIMONIO_LIQUIDO)
        val balancesMap = getValueByTrimester(balances)

        return (lucroLiquido.keys + patrimonioLiquido.keys + balancesMap.keys)
                .distinct()
                .map { key ->
                    BalanceTrimesterInformation(
                            balanceId = getBalanceId(patrimonioLiquido, key, lucroLiquido, balancesMap),
                            cvmCode = cvmCode,
                            year = key.split("-")[0].toInt(),
                            trimester = key.split("-")[1].toInt(),
                            netProfit = lucroLiquido[key]?.value,
                            netWealth = patrimonioLiquido[key]?.value,
                            socialCapitalEntity = convertSocialCapital(balancesMap[key])
                    )
                }
                .sortedBy { it.trimester }
                .sortedBy { it.year }
    }

    private fun getBalanceId(patrimonioLiquido: Map<String, ContaAjustada>, key: String, lucroLiquido: Map<String, ContaAjustada>, balancesMap: Map<String, Balanco>): Long {
        return listOfNotNull(
                patrimonioLiquido[key]?.balanceId,
                lucroLiquido[key]?.balanceId,
                balancesMap[key]?.numeroDocumento)
                .max() ?: throw IllegalStateException("Could not find a balance id")
    }

    private fun convertSocialCapital(balance: Balanco?): SocialCapitalEntity? {
        return if (balance != null) {
            val socialCapital = balance.socialCapital
            SocialCapitalEntity(socialCapital.general, socialCapital.treasury)
        } else {
            null
        }
    }

    private fun getContaByTrimester(contasAjustadas: List<ContaAjustada>, conta: ContasEnum): Map<String, ContaAjustada> {
        val filtered = contasAjustadas.filter { it.conta == conta }

        return filtered
                .groupBy { "${it.year}-${it.trimester}" }
                .mapValues { it.value.first() }
    }

    private fun getValueByTrimester(balances: List<Balanco>): Map<String, Balanco> {
        return balances
                .map { balance ->
                    val trimester = balance.dataReferenciaDocumento.monthValue / 3
                    val year = balance.dataReferenciaDocumento.year

                    Pair("$year-$trimester", balance)
                }
                .toMap()
    }

}
