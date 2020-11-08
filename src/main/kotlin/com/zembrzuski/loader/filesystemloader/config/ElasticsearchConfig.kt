package com.zembrzuski.loader.filesystemloader.config

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticsearchConfig {

    @Bean
    fun restHighLevelClient(): RestHighLevelClient {
        return RestHighLevelClient(RestClient.builder(HttpHost("localhost", 9200, "http")))
    }

}
