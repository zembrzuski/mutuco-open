package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformationEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import com.zembrzuski.loader.filesystemloader.repository.filesystem.GeneralCompanyInformationFilesystemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GeneralCompanyInformationParsing @Autowired constructor(
        private val extractor: GeneralCompanyInformationExtractor) {

    fun parse(input: String, cvmCode: String): GeneralCompanyInformation {
        return GeneralCompanyInformation(
                cvmCode,
                createSectorClassification(input, cvmCode),
                extractor.extractAcceptEmptyProperty(GeneralCompanyInformationEnum.SITE, input, cvmCode, 2),
                extractor.extract(GeneralCompanyInformationEnum.MAIN_ACTIVITY, input, cvmCode, 2),
                extractor.extractNegotiationCode(GeneralCompanyInformationEnum.NEGOCIATION_CODE, input, cvmCode),
                getNegotiationName(input, cvmCode)
        )
    }

    private fun createSectorClassification(input: String, cvmCode: String): SectorClassification {
        val sectorList = extractor
                .extract(GeneralCompanyInformationEnum.SECTOR, input, cvmCode, 2)
                .split("/")

        return SectorClassification(sectorList[0].trim(), sectorList[1].trim(), sectorList[2].trim())
    }

    private fun getNegotiationName(input: String, cvmCode: String): String {
        return extractor
                .extract(GeneralCompanyInformationEnum.NEGOCIATION_NAME, input, cvmCode, 2)
                .split(" ")
                .joinToString(" ") { it.toLowerCase().capitalize() }
    }

}
