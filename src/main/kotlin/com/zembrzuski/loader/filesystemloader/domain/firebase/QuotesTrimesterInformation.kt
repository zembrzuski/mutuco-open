package com.zembrzuski.loader.filesystemloader.domain.firebase

data class QuotesTrimesterInformation(
        val cvmCode: String,
        val stockCode: String,
        val year: Int,
        val trimester: Int,
        val realValue: Float,
        val adjustedValue: Float
)