package com.assembly.vote.assembly.domain.application.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CpfValidClient {

    @Value("${app.url.validcpf}")
    private String baseUrl;

    @Bean
    public WebClient flowClient(){
        return WebClient.create(baseUrl);
    }
}
