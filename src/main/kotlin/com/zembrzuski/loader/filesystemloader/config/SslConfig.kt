package com.zembrzuski.loader.filesystemloader.config

import com.zembrzuski.loader.filesystemloader.util.TrustManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

@Configuration
class SslConfig {

    @Bean
    fun insecureSsl() {
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, arrayOf(TrustManager()), java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    }

}