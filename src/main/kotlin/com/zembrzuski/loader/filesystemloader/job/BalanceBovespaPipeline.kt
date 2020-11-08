package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.exception.InvalidCompanyException
import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalanceRetrievalStatus
import com.zembrzuski.loader.filesystemloader.domain.mydomain.CvmCodesEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.FileStatus
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.CompanyAllInformationImporter
import com.zembrzuski.loader.filesystemloader.service.balance.BalanceDownloader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

/**
 * Pipeline para fazer o download e a importação de dados de balanços da Bovespa.
 */
@Component
class BalanceBovespaPipeline @Autowired constructor(
        val companyAllInformationImporter: CompanyAllInformationImporter,
        val balancesFilesystemRepository: BalancesFilesystemRepository,
        val balanceDownloader: BalanceDownloader) {

    private val errorMessage = "balance retrieval status is in an invalid format: " +
            "FileStatus shows that it is a balance but haven't populated the cvm code"

    fun execute(): FileStatus {
        val balanceRetrievalStatus = balanceDownloader.downloadBalanceFromBovespa()

        return when (balanceRetrievalStatus.fileStatus) {
            FileStatus.FILE_IS_A_BALANCE -> {
                callCompanyImporter(balanceRetrievalStatus)
                FileStatus.FILE_IS_A_BALANCE
            }
            FileStatus.FILE_IS_NOT_A_BALANCE -> {
                println("File is not a balance. Doing nothing.")
                FileStatus.FILE_IS_NOT_A_BALANCE
            }
            FileStatus.FILE_NOT_FOUND -> {
                println("File not found. Doing nothing.")
                balancesFilesystemRepository.createFakeFile(balanceRetrievalStatus.balanceId)
                FileStatus.FILE_NOT_FOUND
            }
        }
    }

    private fun callCompanyImporter(balanceRetrievalStatus: BalanceRetrievalStatus) {
        val cvmCode = balanceRetrievalStatus.cvmCode ?: throw IllegalStateException(errorMessage)

        if (cvmCode == CvmCodesEnum.CAMIL.cvmCode) {
            println("!!! Camil. Não vou importar")
            return
        }

        try {
            companyAllInformationImporter.importCompanyInformation(cvmCode)
        } catch (invalidCompanyException: InvalidCompanyException) {
            println("!!! Erro ao importar companhia cujo codigo cvm é: $cvmCode")
        }
    }

}
