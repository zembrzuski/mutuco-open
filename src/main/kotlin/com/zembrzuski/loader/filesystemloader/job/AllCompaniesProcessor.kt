package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.exception.InvalidCompanyException
import com.zembrzuski.loader.filesystemloader.domain.mydomain.CvmCodesEnum
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.CompanyAllInformationImporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Importa (ou reimpora) todas as informações de todas as empresas.
 */
@Component
class AllCompaniesProcessor @Autowired constructor(
        private val balancesRepo: BalancesFilesystemRepository,
        private val companyAllInformationImporter: CompanyAllInformationImporter) {

    fun importAll() {
//        companyAllInformationImporter.importCompanyInformation(CvmCodesEnum.DIMED.cvmCode)

        balancesRepo.retrieveAllCvmCodes()
                //.filter { it > "091073" }
                .forEach { cvmCode ->
                    try {
                        companyAllInformationImporter.importCompanyInformation(cvmCode)
                    } catch (exception: InvalidCompanyException) {
                        println(exception.message)
                    }
                }

        println("finished importation")
    }

}
