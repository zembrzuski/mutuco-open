package com.zembrzuski.loader.filesystemloader

import com.zembrzuski.loader.filesystemloader.job.AllCompaniesProcessor
import com.zembrzuski.loader.filesystemloader.job.BalanceBovespaPipeline
import com.zembrzuski.loader.filesystemloader.job.GeneralCompanyInformationService
import com.zembrzuski.loader.filesystemloader.service.quotes.IbovQuotesImporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class FilesystemLoaderApplication {

    @Autowired
    private lateinit var balanceBovespaPipeline: BalanceBovespaPipeline

    @Autowired
    private lateinit var allCompaniesProcessor: AllCompaniesProcessor

    @Autowired
    private lateinit var ibovQuotesImporter: IbovQuotesImporter

    @Bean
    fun init() = CommandLineRunner {
        println("initializing...")

        /**
         * Import ibov quotes.
         */
//        ibovQuotesImporter.doImport()

        /**
         * Loop for update balances so far they are published
         */
        while (true) {
            balanceBovespaPipeline.execute()
        }


        /**
         * Reprocess all companies
         */
//        allCompaniesProcessor.importAll()
    }

}

fun main(args: Array<String>) {
    runApplication<FilesystemLoaderApplication>(*args)
}
