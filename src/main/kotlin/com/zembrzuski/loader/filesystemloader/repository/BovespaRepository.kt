package com.zembrzuski.loader.filesystemloader.repository

import com.zembrzuski.loader.filesystemloader.util.RandomNameGenerator
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL

@Component
class BovespaRepository @Autowired constructor(val randomNameGenerator: RandomNameGenerator) {

    private val hugeNumber = 1000000000

    private val balancoUrlBase =
            "https://www.rad.cvm.gov.br/enetconsulta/frmDownloadDocumento.aspx" +
            "?CodigoInstituicao=2" +
            "&NumeroSequencialDocumento="

    private val companyGenericInforationUrlBase =
            "http://bvmf.bmfbovespa.com.br/pt-br/mercados/acoes/empresas/ExecutaAcaoConsultaInfoEmp.asp" +
            "?CodCVM="

    fun downloadDocumentoFromBovespa(documentId: Long): String {
        val name = randomNameGenerator.randomName().plus(".zip")

        FileUtils.copyURLToFile(
                URL(balancoUrlBase.plus(documentId)),
                File(name),
                hugeNumber,
                hugeNumber)

        return name
    }

    fun downloadGenericCompanyInformation(cvmCode: String): String {
        val url = "$companyGenericInforationUrlBase$cvmCode"

        return URL(url).readText()
    }

}
