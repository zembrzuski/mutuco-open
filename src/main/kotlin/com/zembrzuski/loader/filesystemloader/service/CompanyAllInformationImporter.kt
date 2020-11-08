package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.mydomain.toFirebaseCompany
import com.zembrzuski.loader.filesystemloader.repository.FirebaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Importa todas informações pertinentes a uma empresa: balanços, dados administrativos, cotações.
 */
@Component
class CompanyAllInformationImporter @Autowired constructor(
        private val firebaseRepository: FirebaseRepository,
        private val companyAllInformationConverter: CompanyAllInformationConverter) {

    fun importCompanyInformation(cvmCode: String): Boolean {
        println("trying to import company whose cmv code is: $cvmCode")

        val (companyInformation, fullIndicatorsSequence) = companyAllInformationConverter.convert(cvmCode)

        firebaseRepository.persistTrimesterInformationSequence(cvmCode, fullIndicatorsSequence)
        firebaseRepository.persistCompany(companyInformation.toFirebaseCompany())

        println("imported successfully $cvmCode")

        return true
    }

}
