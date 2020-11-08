package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.repository.filesystem.FilesystemHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Classe reponsável por buscar cotações na Alpha Advantages.
 */
@Component
class QuotesPersistence @Autowired constructor(private val filesystemHelper: FilesystemHelper) {

    @Value("\${quotesDirectory}")
    lateinit var quotesDirectory: String

    fun getFilesystemTimeSeries(cvmCode: String, negotiationCode: String): String? {
        val path = "$quotesDirectory/$cvmCode/$negotiationCode.txt"
        return filesystemHelper.readText(path)
    }

    fun persist(
            cvmCode: String,
            negotiationCode: String,
            json: String): Boolean {

        val path = "$quotesDirectory/$cvmCode/$negotiationCode.txt"

        return filesystemHelper.writeText(path, json)
    }

}
