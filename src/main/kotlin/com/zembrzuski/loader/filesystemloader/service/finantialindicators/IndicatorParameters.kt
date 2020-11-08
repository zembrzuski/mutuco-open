package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation

data class IndicatorParameters(
        val trimester: TrimesterInformation,
        val companyInformation: GeneralCompanyInformation
)