package com.zembrzuski.loader.filesystemloader.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Classe para configurar coisas aleatórias, tb conhecido como saco de gato.
 */
@Configuration
class OtherConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }

}