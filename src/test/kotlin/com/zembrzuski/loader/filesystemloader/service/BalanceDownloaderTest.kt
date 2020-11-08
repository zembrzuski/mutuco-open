package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.bovespa.CompanhiaAberta
import com.zembrzuski.loader.filesystemloader.domain.bovespa.FormularioCadastral
import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalanceRetrievalStatus
import com.zembrzuski.loader.filesystemloader.domain.mydomain.FileStatus
import com.zembrzuski.loader.filesystemloader.repository.BovespaRepository
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.balance.BalanceDownloader
import com.zembrzuski.loader.filesystemloader.util.XmlParser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class BalanceDownloaderTest {

    @Before fun init() = MockKAnnotations.init(this)

    @Test
    fun moveBalanceForDfp() {
        // given
        val maps = mapOf("FormularioDemonstracaoFinanceiraDFP.xml" to "xoxo")
        val formularioCadastralString = "xaxa"
        val formularioCadastral = FormularioCadastral(CompanhiaAberta("123-456", "ae"), "fakeDate")

        val filesystemRepository = mockk<BalancesFilesystemRepository>()
        val bovespaRepository= mockk<BovespaRepository>()
        val xmlParser = mockk<XmlParser>()
        val balanceId = 66L

        every { bovespaRepository.downloadDocumentoFromBovespa(balanceId) } returns "file.zip"
        every { filesystemRepository.openZip("file.zip") } returns maps
        every { xmlParser.getKey("-FormularioCadastral.xml", maps) } returns formularioCadastralString
        every { xmlParser.parseFormularioCadastral(formularioCadastralString) } returns formularioCadastral
        every { filesystemRepository.copyDemonstrativoToCorrectPath(balanceId, "file.zip", "123456") } returns true

        var downloader = BalanceDownloader(filesystemRepository, bovespaRepository, xmlParser)

        // when
        val result = downloader.downloadById(balanceId)


        // then
        assertEquals(result, BalanceRetrievalStatus(FileStatus.FILE_IS_A_BALANCE, "123456", balanceId) )
    }

    @Test
    fun moveBalanceForItr() {
        // given
        val maps = mapOf("FormularioDemonstracaoFinanceiraITR.xml" to "xoxo")
        val formularioCadastralString = "xaxa"
        val formularioCadastral = FormularioCadastral(CompanhiaAberta("123-456", "ae"), "fakeDate")

        val filesystemRepository = mockk<BalancesFilesystemRepository>()
        val bovespaRepository= mockk<BovespaRepository>()
        val xmlParser = mockk<XmlParser>()
        val balanceId = 66L

        every { bovespaRepository.downloadDocumentoFromBovespa(balanceId) } returns "file.zip"
        every { filesystemRepository.openZip("file.zip") } returns maps
        every { xmlParser.getKey("-FormularioCadastral.xml", maps) } returns formularioCadastralString
        every { xmlParser.parseFormularioCadastral(formularioCadastralString) } returns formularioCadastral
        every { filesystemRepository.copyDemonstrativoToCorrectPath(balanceId, "file.zip", "123456") } returns true

        var downloader = BalanceDownloader(filesystemRepository, bovespaRepository, xmlParser)

        // when
        val result = downloader.downloadById(balanceId)

        // then
        assertEquals(result, BalanceRetrievalStatus(FileStatus.FILE_IS_A_BALANCE, "123456", balanceId) )
    }

    @Test
    fun removeFileForEmptyMap() {
        // given
        val maps = mapOf<String, String>()
        val formularioCadastralString = "xaxa"
        val formularioCadastral = FormularioCadastral(CompanhiaAberta("123-456", "ae"), "fakeDate")

        val filesystemRepository = mockk<BalancesFilesystemRepository>()
        val bovespaRepository= mockk<BovespaRepository>()
        val xmlParser = mockk<XmlParser>()
        val balanceId = 66L

        every { bovespaRepository.downloadDocumentoFromBovespa(balanceId) } returns "file.zip"
        every { filesystemRepository.openZip("file.zip") } returns maps
        every { xmlParser.getKey("-FormularioCadastral.xml", maps) } returns formularioCadastralString
        every { xmlParser.parseFormularioCadastral(formularioCadastralString) } returns formularioCadastral
        every { filesystemRepository.delete("file.zip") } returns true

        var downloader = BalanceDownloader(filesystemRepository, bovespaRepository, xmlParser)

        // when
        val result = downloader.downloadById(balanceId)

        // then
        assertEquals(result, BalanceRetrievalStatus(FileStatus.FILE_NOT_FOUND, balanceId = balanceId) )
    }

    @Test
    fun moveBalanceForLimboWhenIsAZipAndIsNotDfpNorItr() {
        // given
        val maps = mapOf("arquivosToscos.xml" to "xoxo")

        val filesystemRepository = mockk<BalancesFilesystemRepository>()
        val bovespaRepository= mockk<BovespaRepository>()
        val xmlParser = mockk<XmlParser>()
        val balanceId = 66L

        every { bovespaRepository.downloadDocumentoFromBovespa(balanceId) } returns "file.zip"
        every { filesystemRepository.openZip("file.zip") } returns maps
        every { filesystemRepository.copyDemonstrativoToLimbo(balanceId, "file.zip") } returns true

        var downloader = BalanceDownloader(filesystemRepository, bovespaRepository, xmlParser)

        // when
        val result = downloader.downloadById(balanceId)

        // then
        assertEquals(result, BalanceRetrievalStatus(FileStatus.FILE_IS_NOT_A_BALANCE, null, balanceId) )
    }

}