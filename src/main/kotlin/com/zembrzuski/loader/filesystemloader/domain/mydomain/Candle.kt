package com.zembrzuski.loader.filesystemloader.domain.mydomain

import java.time.LocalDate

data class Candle(
        val date: LocalDate,
        val open: Float,
        val high: Float,
        val low: Float,
        val close: Float,
        val adjustedClose: Float,
        val volume: Long
)