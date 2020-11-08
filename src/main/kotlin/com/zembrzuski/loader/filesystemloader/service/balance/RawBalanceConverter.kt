package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Dado o codigo cvm de uma empresa, carrega todos seus balanços
 * zipados do filesystem para uma lista de Balanco
 */
@Component
class RawBalanceConverter @Autowired constructor(
        val balancesFilesystemRepository: BalancesFilesystemRepository,
        val balanceLoader: BalanceLoader,
        val balanceConverter: BalanceConverter) {

    fun loadRawBalances(cvmCode: String): List<Balanco> {
        return balancesFilesystemRepository
                .findAllBalancesFromCompany(cvmCode)
                .map { balanceLoader.loadBalance(it, cvmCode) }
                .map { balanceConverter.convert(it) }
    }

}
