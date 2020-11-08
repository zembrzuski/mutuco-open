package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Scale
import org.springframework.stereotype.Component

@Component
class ScaleConverter {

    fun convert(valueScale: Int): Scale {
        return Scale.values().first { it.bovespaRepresentation == valueScale }
    }

}
