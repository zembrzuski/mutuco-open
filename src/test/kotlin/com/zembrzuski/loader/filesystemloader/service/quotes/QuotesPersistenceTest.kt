package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.repository.GeneralHttpRepository
import com.zembrzuski.loader.filesystemloader.repository.filesystem.FilesystemHelper
import com.zembrzuski.loader.filesystemloader.util.Sleeper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QuotesPersistenceTest {

    @MockK lateinit var generalHttpRepository: GeneralHttpRepository
    @MockK lateinit var sleeper: Sleeper
    @MockK lateinit var filesystemHelper: FilesystemHelper
    @InjectMockKs lateinit var  quotesPersistence: QuotesPersistence

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getFilesystemTimeSeries() {
        // given
        val cvmCode = "XXX"
        val negotitationCode = "YYY"
        quotesPersistence.quotesDirectory = "dir"

        every { filesystemHelper.readText("dir/XXX/YYY.txt") } returns "achou"

        // when
        val result = quotesPersistence.getFilesystemTimeSeries(cvmCode, negotitationCode)

        // then
        assertEquals(result, "achou")
    }

    @Test
    fun persistFilesystemTimeSeries() {
        // given
        val cvmCode = "XXX"
        val negotitationCode = "YYY"
        quotesPersistence.quotesDirectory = "dir"
        val json = "json"

        every { filesystemHelper.writeText("dir/XXX/YYY.txt", json) } returns true

        // when
        val result = quotesPersistence.persist(cvmCode, negotitationCode, json)

        // then
        assertTrue(result)
    }

}