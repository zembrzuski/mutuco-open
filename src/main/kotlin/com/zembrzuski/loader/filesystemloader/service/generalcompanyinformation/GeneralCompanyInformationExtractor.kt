package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.exception.InvalidCompanyException
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformationEnum
import org.springframework.stereotype.Component
import org.springframework.web.util.HtmlUtils
import java.lang.IllegalStateException

@Component
class GeneralCompanyInformationExtractor {

    fun extract(
            companyInformation: GeneralCompanyInformationEnum,
            input: String,
            cvmCode: String,
            group: Int): String {

        return extract(companyInformation, input, group)
                ?: throw InvalidCompanyException("Could not find ${companyInformation.name} for company: $cvmCode")
    }

    fun extractAcceptEmptyProperty(
            companyInformation: GeneralCompanyInformationEnum,
            input: String,
            cvmCode: String,
            group: Int): String {

        return extract(companyInformation, input, group) ?: ""
    }

    private fun extract(companyInformation: GeneralCompanyInformationEnum, input: String, group: Int): String? {
        return companyInformation.regex
                .find(preprocessorText(input))
                ?.groupValues
                ?.get(group)
                ?.trim()
    }

    fun extractNegotiationCode(
            companyInformation: GeneralCompanyInformationEnum,
            input: String,
            cvmCode: String): List<String> {

        val iterator = companyInformation.regex
                .findAll(preprocessorText(input))
                .iterator()

        val allCodes = arrayListOf<String>()

        iterator.forEach {
            allCodes.add(it.groupValues[1].trim())
        }

        return allCodes
    }

    private fun preprocessorText(input: String) = HtmlUtils.htmlUnescape(input)
            .replace("\n", "")
            .replace("\t", "")
            .replace("\r", "")

}
