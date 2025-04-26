package com.example.impati.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.messaging-system")
public record SpringMessagingSystemPublisherProperties(
        String url, String clientName
) {

}
