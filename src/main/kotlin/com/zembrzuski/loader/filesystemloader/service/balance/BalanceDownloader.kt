package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalanceRetrievalStatus
import com.zembrzuski.loader.filesystemloader.domain.mydomain.FileStatus
import com.zembrzuski.loader.filesystemloader.repository.BovespaRepository
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.util.XmlParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Download bovespa balances and persist them on filesystem.
 */
@Component
class BalanceDownloader @Autowired constructor(
        val balancesFilesystemRepository: BalancesFilesystemRepository,
        val bovespaRepository: BovespaRepository,
        val xmlParser: XmlParser) {

    fun downloadBalanceFromBovespa(): BalanceRetrievalStatus {
        val maxBalanceId = balancesFilesystemRepository.retrieveMaxBalanceId()
        return downloadById(maxBalanceId + 1)
    }

    fun downloadById(id: Long): BalanceRetrievalStatus {
        println("trying doc id: $id")

        val zipName = bovespaRepository.downloadDocumentoFromBovespa(id)
        val balance = balancesFilesystemRepository.openZip(zipName)

        return when {
            balance.keys.any { itrOrDfpPredicate(it) } -> moveBalance(balance, id, zipName)
            balance.keys.isEmpty() -> removeFile(zipName, id)
            else -> moveFileThatIsNotABalance(id, zipName)
        }
    }

    private fun itrOrDfpPredicate(it: String) = it.toLowerCase().contains("itr") || it.toLowerCase().contains("dfp")

    private fun moveFileThatIsNotABalance(id: Long, zipName: String): BalanceRetrievalStatus {
        balancesFilesystemRepository.copyDemonstrativoToLimbo(id, zipName)

        return BalanceRetrievalStatus(FileStatus.FILE_IS_NOT_A_BALANCE, balanceId = id)
    }

    private fun removeFile(zipName: String, balanceId: Long): BalanceRetrievalStatus {
        balancesFilesystemRepository.delete(zipName)

        return BalanceRetrievalStatus(FileStatus.FILE_NOT_FOUND, balanceId = balanceId)
    }

    private fun moveBalance(balance: Map<String, String>, id: Long, zipName: String): BalanceRetrievalStatus {
        val cvmCode = getCvmCode(balance)
        balancesFilesystemRepository.copyDemonstrativoToCorrectPath(id, zipName, cvmCode)

        return BalanceRetrievalStatus(FileStatus.FILE_IS_A_BALANCE, cvmCode, id)
    }

    private fun getCvmCode(balance: Map<String, String>): String {
        return xmlParser
                .parseFormularioCadastral(xmlParser.getKey("-FormularioCadastral.xml", balance))
                .companhiaAberta
                .codigoCvm
                .replace("-", "")
    }
}
