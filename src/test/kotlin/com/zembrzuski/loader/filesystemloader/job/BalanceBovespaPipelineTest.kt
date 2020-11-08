package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalanceRetrievalStatus
import com.zembrzuski.loader.filesystemloader.domain.mydomain.FileStatus
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.balance.BalanceDownloader
import com.zembrzuski.loader.filesystemloader.service.CompanyAllInformationImporter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test


internal class BalanceBovespaPipelineTest {

    @Test
    fun delegateWhenFileIsABalance() {
        // given
        val balanceImporter = mockk<CompanyAllInformationImporter>()
        val balanceDownloader = mockk<BalanceDownloader>()
        val balancesFilesystemRepository = mockk<BalancesFilesystemRepository>()

        val cvmCode = "123456"
        every { balanceDownloader.downloadBalanceFromBovespa()
        } returns BalanceRetrievalStatus(FileStatus.FILE_IS_A_BALANCE, cvmCode, 1)

        every {
            balanceImporter.importCompanyInformation(cvmCode)
        } returns true
        // when

        val result = BalanceBovespaPipeline(
                balanceImporter,
                balancesFilesystemRepository,
                balanceDownloader
        ).execute()

        // then
        verify { balanceImporter.importCompanyInformation(cvmCode) }
        assertEquals(result, FileStatus.FILE_IS_A_BALANCE)
    }

    @Test
    fun nothingWhenFileIsNotABalance() {
        // given
        val balanceImporter = mockk<CompanyAllInformationImporter>()
        val balanceDownloader = mockk<BalanceDownloader>()
        val balancesFilesystemRepository = mockk<BalancesFilesystemRepository>()

        every { balanceDownloader.downloadBalanceFromBovespa()
        } returns BalanceRetrievalStatus(FileStatus.FILE_IS_NOT_A_BALANCE, null, 1)

        // when
        val result = BalanceBovespaPipeline(balanceImporter,
                balancesFilesystemRepository, balanceDownloader).execute()

        // then
        assertEquals(result, FileStatus.FILE_IS_NOT_A_BALANCE)
    }

}