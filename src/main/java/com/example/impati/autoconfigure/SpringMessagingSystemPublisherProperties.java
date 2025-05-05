package com.example.impati.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.messaging-system")
public record SpringMessagingSystemPublisherProperties(
        String url, String clientName, boolean deliveryGuarantee
) {

}
