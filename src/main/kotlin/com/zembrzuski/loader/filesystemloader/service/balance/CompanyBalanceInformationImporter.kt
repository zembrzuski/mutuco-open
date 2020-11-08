package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Importa dados de balanço de uma determinada empresa.
 */
@Component
class CompanyBalanceInformationImporter @Autowired constructor(
        private val rawBalanceConverter: RawBalanceConverter,
        private val lastBalanceReducer: LastBalanceReducer,
        private val companyBalanceInformationConverter: CompanyBalanceInformationConverter) {

    fun importAllBalancesFromCompany(cvmCode: String): List<BalanceTrimesterInformation> {
        val balancesRaw = rawBalanceConverter.loadRawBalances(cvmCode)
        val reducedRaw = lastBalanceReducer.reduceLastBalanceForEachTrimester(balancesRaw)

        return companyBalanceInformationConverter.convert(cvmCode, reducedRaw)
    }

}
