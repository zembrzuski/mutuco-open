package com.zembrzuski.loader.filesystemloader.domain.mydomain

enum class GeneralCompanyInformationEnum(val regex: Regex) {

    SECTOR(regex("Classifica.*Setorial:</td>(.+?)<td>(.+?)</td>")),
    SITE(regex("Site:</td>(.+?)<td><a.+?href=.+?\">(.+?)</a>")),
    NEGOCIATION_CODE(regex("onclick=javascript:VerificaCotacao\\('(.+?)'")),
    MAIN_ACTIVITY(regex("Atividade Principal:</td>(.+?)<td>(.+?)</td>")),
    NEGOCIATION_NAME(regex("<td>Nome de Preg.o:</td>(.+?)<td>(.+?)</td>"))

}
