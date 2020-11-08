package com.zembrzuski.loader.filesystemloader.domain.mydomain

/**
 * No xml da bovespa, quando a escala da moeda é 2, significa que os valores estão em mil reais.
 * Quando a escala da moeda é 1, significa que os valores estão em reais.
 */
enum class Scale(val bovespaRepresentation: Int, val multiplicationFactor: Float) {
    REAIS(1, 1F),
    MIL_REAIS(2, 1000F)
}
