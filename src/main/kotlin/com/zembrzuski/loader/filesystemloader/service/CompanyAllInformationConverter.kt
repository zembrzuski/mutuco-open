package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.CvmCodesEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.job.GeneralCompanyInformationService
import com.zembrzuski.loader.filesystemloader.service.balance.BalancesAndQuotesMapper
import com.zembrzuski.loader.filesystemloader.service.balance.CompanyBalanceInformationImporter
import com.zembrzuski.loader.filesystemloader.service.balance.LastBalanceReducer
import com.zembrzuski.loader.filesystemloader.service.finantialindicators.FinancialIndicatorsService
import com.zembrzuski.loader.filesystemloader.service.quotes.QuotesImporterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CompanyAllInformationConverter @Autowired constructor(
        private val companyBalanceInformationImporter: CompanyBalanceInformationImporter,
        private val quotesImporterService: QuotesImporterService,
        private val generalCompanyInformationService: GeneralCompanyInformationService,
        private val balancesAndQuotesMapper: BalancesAndQuotesMapper,
        private val financialIndicatorsService: FinancialIndicatorsService,
        private val lastBalanceReducer: LastBalanceReducer) {

    fun convert(cvmCode: String): Pair<GeneralCompanyInformation, List<TrimesterInformation>> {
        val balancesTrimester = companyBalanceInformationImporter.importAllBalancesFromCompany(cvmCode)
        val lastBalance = lastBalanceReducer.reduceBalanceById(balancesTrimester)
        val companyInformation = generalCompanyInformationService.getCompanyInformation(cvmCode, lastBalance)

        println(companyInformation.negotiationName)

        val quotes = quotesImporterService.importAll(cvmCode, companyInformation.stockCodes, balancesTrimester)
        val balancesAndQuotesInformation = balancesAndQuotesMapper.joinBalancesToQuotes(balancesTrimester, quotes)
        val fullIndicatorsSequence = financialIndicatorsService.compute(balancesAndQuotesInformation, companyInformation)

        return Pair(companyInformation, fullIndicatorsSequence)
    }

}
