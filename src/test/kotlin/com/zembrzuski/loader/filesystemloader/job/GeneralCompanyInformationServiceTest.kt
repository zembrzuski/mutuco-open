package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.exception.InvalidCompanyException
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import com.zembrzuski.loader.filesystemloader.repository.BovespaRepository
import com.zembrzuski.loader.filesystemloader.repository.filesystem.GeneralCompanyInformationFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.GeneralCompanyInformationParsing
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class GeneralCompanyInformationServiceTest {

    @Test
    fun companyRetrievedOnFilesystem() {
        // given
        val cvmCode = "1"

        val bovespa = mockk<BovespaRepository>()
        val parser = mockk<GeneralCompanyInformationParsing>()
        val filesystem = mockk<GeneralCompanyInformationFilesystemRepository>()

        val sector = SectorClassification("a", "b", "c")
        val generalCompanyInformation = GeneralCompanyInformation(
                "1", sector, "site", "acitivty", listOf(), "name", lastBalanceId = 322L, lastBalanceDate = "2000-01")

        val lastBalance = BalanceTrimesterInformation(322L, "cvm", 2000, 1)

        every { filesystem.retrieveCompanyInformationOnFileSystem(cvmCode) } returns "infow"
        every { parser.parse("infow", cvmCode) } returns generalCompanyInformation

        // when
        val result = GeneralCompanyInformationService(bovespa, parser, filesystem)
                .getCompanyInformation(cvmCode, lastBalance)

        // then
        assertEquals(result, generalCompanyInformation)
    }

    @Test(expected = InvalidCompanyException::class)
    fun exceptionForUnavailableData() {
        // bovespaRepository.downloadGenericCompanyInformation(cvmCode) -> dados indisponiveis
        // given
        val cvmCode = "1"

        val bovespa = mockk<BovespaRepository>()
        val parser = mockk<GeneralCompanyInformationParsing>()
        val filesystem = mockk<GeneralCompanyInformationFilesystemRepository>()
        val lastBalance = BalanceTrimesterInformation(1L, "cvm", 2000, 1)


        every { filesystem.retrieveCompanyInformationOnFileSystem(cvmCode) } returns null
        every { bovespa.downloadGenericCompanyInformation(cvmCode) } returns "bla bla bla dados indisponiveis. xo xo"

        // when
        GeneralCompanyInformationService(bovespa, parser, filesystem).getCompanyInformation(cvmCode, lastBalance)
    }

    @Test
    fun companyRetrievedOnBovespa() {
        // given
        val cvmCode = "1"

        val bovespa = mockk<BovespaRepository>()
        val parser = mockk<GeneralCompanyInformationParsing>()
        val filesystem = mockk<GeneralCompanyInformationFilesystemRepository>()

        val sector = SectorClassification("a", "b", "c")
        val generalCompanyInformation = GeneralCompanyInformation(
                "1", sector, "site", "acitivty", listOf(), "name", lastBalanceId = 321L, lastBalanceDate = "2000-01"
        )

        val lastBalance = BalanceTrimesterInformation(321L,"cvm", 2000, 1)

        every { filesystem.retrieveCompanyInformationOnFileSystem(cvmCode) } returns null
        every { bovespa.downloadGenericCompanyInformation(cvmCode) } returns "infow"
        every { filesystem.persist("infow", cvmCode) } returns true
        every { parser.parse("infow", cvmCode) } returns generalCompanyInformation

        // when
        val result = GeneralCompanyInformationService(bovespa, parser, filesystem).getCompanyInformation(cvmCode, lastBalance)

        // then
        assertEquals(result, generalCompanyInformation)
    }

}
