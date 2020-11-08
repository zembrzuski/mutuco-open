package com.zembrzuski.loader.filesystemloader.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig {

    @Bean
    fun firestore(): Firestore? {
        val fileInputStream = FileInputStream("./prd-zem-finance-firebase-adminsdk-c26bn-f8362ef06a.json")

        val firestoreOptions = FirestoreOptions
                .newBuilder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()

        val options = FirebaseOptions.builder()
                .setFirestoreOptions(firestoreOptions)
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .build()

        FirebaseApp.initializeApp(options)

        return FirestoreClient.getFirestore()
    }

}