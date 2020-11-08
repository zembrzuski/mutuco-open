package com.zembrzuski.loader.filesystemloader.repository.filesystem

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *
 * Classe responsável pela persistência e recuperação de dados gerais
 * de empresas (nome, segmento, papéis) no filesystem.
 *
 */
@Component
class GeneralCompanyInformationFilesystemRepository @Autowired constructor(
        private val filesystemHelper: FilesystemHelper) {

    @Value("\${generalInformationDirectory}")
    lateinit var generalInformationDirectory: String

    fun retrieveCompanyInformationOnFileSystem(cvmCode: String): String? {
        return filesystemHelper.readText("$generalInformationDirectory/$cvmCode/info.html")
    }

    fun persist(companyInformation: String, cvmCode: String): Boolean {
        filesystemHelper.writeText("$generalInformationDirectory/$cvmCode/info.html", companyInformation)

        return true
    }

}
