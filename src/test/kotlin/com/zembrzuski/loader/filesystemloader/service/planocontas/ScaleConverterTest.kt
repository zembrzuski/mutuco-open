package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Scale
import org.junit.Assert.*
import org.junit.Test

class ScaleConverterTest {

    @Test
    fun someTests() {
        val converter = ScaleConverter()

        assertEquals(converter.convert(1), Scale.REAIS)
        assertEquals(converter.convert(2), Scale.MIL_REAIS)
    }

}