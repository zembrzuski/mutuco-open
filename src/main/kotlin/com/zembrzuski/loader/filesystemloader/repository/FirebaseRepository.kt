package com.zembrzuski.loader.filesystemloader.repository

import com.google.cloud.firestore.Firestore
import com.zembrzuski.loader.filesystemloader.domain.firebase.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FirebaseRepository @Autowired constructor(private val firestore: Firestore) {

    fun persistCompany(company: Company) {
        save("company", company.cvmCode, company)
    }

    fun persistTrimesterInformationSequence(
            cvmCode: String, balanceTrimesterInformationSequence: List<TrimesterInformation>) {

        balanceTrimesterInformationSequence.forEach {
            val key = "$cvmCode-${it.balanceTrimesterInformation.year}-${it.balanceTrimesterInformation.trimester}"
            save("trimester-information", key, it)
        }
    }

    private fun save(collection: String, id: String, pojo: Any) {
        firestore
                .collection(collection)
                .document(id)
                .set(pojo)
                .get()
    }

    fun persistIbov(trimestralQuote: List<QuotesTrimesterInformation>) {
        firestore
                .collection("ibov")
                .document("carteira")
                .set(IbovQuote(trimestralQuote))
                .get()
    }

}
