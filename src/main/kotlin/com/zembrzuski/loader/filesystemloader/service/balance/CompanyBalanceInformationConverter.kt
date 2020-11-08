package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CompanyBalanceInformationConverter @Autowired constructor(
        private val balancesToAccountSequenceConverter: BalancesToContaSequenceConverter,
        private val accountSequenceFitter: AccountSequenceFitter,
        private val trimesterInformationConverter: TrimesterInformationConverter,
        private val socialCapitalScaler: SocialCapitalScaler) {

    fun convert(cvmCode: String, balances: List<Balanco>): List<BalanceTrimesterInformation> {
        val accountByTrimester = balancesToAccountSequenceConverter.getContasPorTrimestre(balances)
        val adjustedAccounts = accountSequenceFitter.fit(accountByTrimester, cvmCode)

        val balancesWithScaledSocialCapital = balances.map { balance -> socialCapitalScaler.scale(balance) }

        return trimesterInformationConverter.convert(cvmCode, adjustedAccounts, balancesWithScaledSocialCapital)
    }

}
