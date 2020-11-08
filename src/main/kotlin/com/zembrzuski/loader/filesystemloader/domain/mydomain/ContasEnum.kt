package com.zembrzuski.loader.filesystemloader.domain.mydomain

enum class ContasEnum(
        val contaName: String,
        val regex: Regex,
        val tipoConta: TipoContaEnum,
        val contaPrimeiroDigito: String) {

    PATRIMONIO_LIQUIDO(
            "PatrimonioLiquido",
            regex("^patr(\\.)?(im[oô]nio)? l[ií]q(\\.)?(uido)?\$"),
            TipoContaEnum.BALANCO,
            "2"),

    LUCRO_LIQUIDO(
            "Lucro",
            regex("^lucro.preju.zo do per.odo"),
            TipoContaEnum.DEMONSTRATIVO,
            "3")

}

fun regex(regex: String): Regex {
    return Regex(regex, RegexOption.IGNORE_CASE)
}
