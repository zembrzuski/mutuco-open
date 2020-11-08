package com.zembrzuski.loader.filesystemloader.repository.filesystem

import com.google.api.client.util.Maps
import com.zembrzuski.loader.filesystemloader.util.RandomNameGenerator
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.util.zip.ZipException
import java.util.zip.ZipFile

/**
 * TODO essa classe tá ridiculamente grande. Quebrá-la.
 *
 * Classe responsável pela persistência e recuperação de balanços no filesystem.
 */
@Component
class BalancesFilesystemRepository @Autowired constructor(
        private val randomNameGenerator: RandomNameGenerator,
        private val filesystemHelper: FilesystemHelper) {

    @Value("\${balancesDirectory}")
    lateinit var balancesDirectory: String

    fun copyDemonstrativoToCorrectPath(id: Long, zipName: String, cvmCode: String): Boolean {
        moveFile(zipName, "$balancesDirectory/$cvmCode/$id.zip")
        return true
    }

    fun copyDemonstrativoToLimbo(id: Long, zipName: String): Boolean {
        moveFile(zipName, "$balancesDirectory/limbo/$id.zip")
        return true
    }

    fun createFakeFile(id: Long) {
        val path = "$balancesDirectory/limbo/$id.zip"
        val file = File(path)
        file.createNewFile()
    }

    fun findAllBalancesFromCompany(cvmCode: String): List<Long> {
        return File("$balancesDirectory/$cvmCode")
                .list(zipFilter)
                .filterNotNull()
                .map { it.replace(".zip", "") }
                .map { it.toLong() }
                .sorted()
    }

    fun openZip(zipFilePath: String): Map<String, String> {
        return try {
            extractXmls(zipFilePath, "").toMap()
        } catch (e: ZipException) {
            Maps.newHashMap()
        }
    }

    fun openBalanco(cvmCode: String, numeroDocumento: Long): Map<String, String> {
        val zipFilePath = "$balancesDirectory/$cvmCode/$numeroDocumento.zip"
        val zipFile = ZipFile(zipFilePath)

        val innerXmls = zipFile.entries()
                .toList()
                .filter { zipEntry -> zipEntry.name.endsWith("dfp") || zipEntry.name.endsWith("itr") }
                .map { zipEntry -> Pair(zipEntry.name, zipFile.getInputStream(zipEntry)) }
                .flatMap { pair -> openInnerZip(pair.second, pair.first) }

        val outerXmls = extractXmls(zipFilePath, "outer")

        return joinTwoLists(innerXmls, outerXmls).toMap()
    }

    fun retrieveAllCvmCodes(): List<String> {
        return File(balancesDirectory)
                .listFiles()
                .mapNotNull { it.name }
                .filter { "limbo" != it }
                .sorted()
    }

    fun retrieveMaxBalanceId(): Long {
        return File(balancesDirectory)
                .listFiles()
                .flatMap { listFilesFromCompany(it) }
                .filter { it.endsWith("zip") }
                .map { it.split("/").last().split(".zip").first() }
                .map { it.toLong() }
                .max()
                ?: throw IllegalStateException(
                        "error trying to find max balance id. Maybe you haven't set " +
                        "your filepath correctly or maybe you haven't downloaded any balance yet")
    }

    private fun listFilesFromCompany(it: File): List<String> {
        return it.listFiles()
                .filterNotNull()
                .map { it.absolutePath }
    }

    private fun joinTwoLists(
            innerXmls: List<Pair<String, String>>,
            outerXmls: List<Pair<String, String>>):
            List<Pair<String, String>> {

        val joined = ArrayList<Pair<String, String>>()

        joined.addAll(innerXmls)
        joined.addAll(outerXmls)

        return joined
    }

    private fun extractXmls(zipFilePath: String, filePrefix: String): List<Pair<String, String>> {
        val zipFile = ZipFile(zipFilePath)

        return zipFile
                .entries()
                .toList()
                .filter { zipEntry -> zipEntry.name.endsWith("xml") }
                .map { zipEntry -> Pair("$filePrefix-$zipEntry" , zipFile.getInputStream(zipEntry)) }
                .map { pair -> Pair(pair.first, inputStreamToText(pair.second)) }
    }

    private fun openInnerZip(inputStream: InputStream?, fileName: String): List<Pair<String, String>> {
        val filePrefix = fileName.split(".").last()
        val filename = randomNameGenerator.randomName().plus(".zip")

        inputStream?.toFile(filename)
        val extractXmls = extractXmls(filename, filePrefix)
        File(filename).delete()
        return extractXmls
    }

    private fun inputStreamToText(inputStream: InputStream?): String {
        val reader = BufferedReader(inputStream?.reader())
        val text = reader.readText()
        reader.close()

        return text
    }

    private val zipFilter = { _: File, fileName: String ->
        fileName.endsWith(".zip")
    }

    private fun InputStream.toFile(path: String) {
        File(path).delete()
        File(path).outputStream().use { this.copyTo(it) }
    }

    private fun moveFile(origin: String, destination: String) {
        val file = File(origin)

        filesystemHelper.createPath(destination)

        FileUtils.copyFile(file, File(destination))
        file.delete()
    }

    fun delete(zipName: String): Boolean {
        File(zipName).delete()
        return true
    }

}
