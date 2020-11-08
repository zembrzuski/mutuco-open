package com.zembrzuski.loader.filesystemloader.repository.filesystem

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Component
import java.io.File

/**
 * Componentes comuns aos repositories de filesystem.
 */
@Component
class FilesystemHelper {

    fun readText(path: String): String? {
        return if (File(path).exists()) File(path).readText() else null
    }

    fun writeText(path: String, fileContent: String): Boolean {
        createPath(path)
        File(path).delete()
        FileUtils.writeStringToFile(File(path), fileContent, "UTF-8")

        return true
    }

    fun createPath(filePath: String) {
        val directoryPath = filePath.split("/")
                .toList()
                .dropLast(1)
                .joinToString("/")

        File(directoryPath).mkdirs()
    }

}
