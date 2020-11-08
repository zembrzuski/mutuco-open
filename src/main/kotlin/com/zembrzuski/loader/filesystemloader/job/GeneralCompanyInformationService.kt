package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.exception.InvalidCompanyException
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.repository.BovespaRepository
import com.zembrzuski.loader.filesystemloader.repository.filesystem.GeneralCompanyInformationFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.GeneralCompanyInformationParsing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Faz download e a persistência de dados de gerais de empresas da Bovespa.
 *
 * Se já tem as informações em filesystem, usa elas. Se não tem, baixa da bovespa.
 *
 * Dados gerais são coisas como classificação setorial, atividade principal, site, códigos
 * de negociaçao.
 */
@Component
class GeneralCompanyInformationService @Autowired constructor(
        private val bovespaRepository: BovespaRepository,
        private val generalInformationParsing: GeneralCompanyInformationParsing,
        private val filesystemRepository: GeneralCompanyInformationFilesystemRepository) {

    fun getCompanyInformation(cvmCode: String, lastBalance: BalanceTrimesterInformation): GeneralCompanyInformation {
        return generalInformationParsing
                .parse(retrieveCompanyInformation(cvmCode), cvmCode)
                .copy(lastBalanceId = lastBalance.balanceId, lastBalanceDate = getLastBalanceDate(lastBalance))
    }

    fun getLastBalanceDate(lastBalance: BalanceTrimesterInformation): String? {
        /**
         * TODO test
         */
        val trimester = lastBalance.trimester.toString().padStart(2, '0')

        return "${lastBalance.year}-${trimester}"
    }

    private fun retrieveCompanyInformation (cvmCode: String): String {
        val companyInformation =
                filesystemRepository.retrieveCompanyInformationOnFileSystem(cvmCode)

        if (companyInformation != null) {
            return companyInformation
        }

        val retrievedHtml = bovespaRepository.downloadGenericCompanyInformation(cvmCode)

        if (retrievedHtml.contains("Dados indisponiveis.", ignoreCase = true)) {
            throw InvalidCompanyException("não consegui dados para a empresa $cvmCode")
        }

        filesystemRepository.persist(retrievedHtml, cvmCode)

        return retrievedHtml
    }
}
