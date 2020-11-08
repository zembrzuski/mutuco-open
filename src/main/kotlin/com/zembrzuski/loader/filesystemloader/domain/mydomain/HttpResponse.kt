package com.zembrzuski.loader.filesystemloader.domain.mydomain

data class HttpResponse(
        val text: String,
        val cookies: Map<String, String>
)
