package com.example.impati.autoconfigure;

import com.example.impati.messaging_system_publisher.core.ChannelRegister;
import com.example.impati.messaging_system_publisher.core.ChannelRegistration;
import com.example.impati.messaging_system_publisher.core.ClientRegister;
import com.example.impati.messaging_system_publisher.core.MessagingSystemClient;
import com.example.impati.messaging_system_publisher.core.MessagingSystemProperties;
import com.example.impati.messaging_system_publisher.core.Publisher;
import com.example.impati.messaging_system_publisher.core.SimpleChannelRegister;
import com.example.impati.messaging_system_publisher.core.SimpleClientRegister;
import com.example.impati.messaging_system_publisher.core.SimplePublisher;
import com.example.impati.messaging_system_publisher.core.WebClientMessagingSystemClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(SpringMessagingSystemPublisherProperties.class)
public class MessagingSystemPublisherAutoConfiguration {

    private final SpringMessagingSystemPublisherProperties properties;

    public MessagingSystemPublisherAutoConfiguration(SpringMessagingSystemPublisherProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ChannelRegistration channelRegistration() {
        return new ChannelRegistration.ChannelRegistrationBuilder().build();
    }

    @Bean
    @ConditionalOnMissingBean
    public MessagingSystemProperties properties() {
        return new MessagingSystemProperties(properties.url(), properties.clientName());
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient.Builder webClient() {
        return new WebClientConfig().webClientBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientRegister clientRegister(MessagingSystemClient client) {
        return new SimpleClientRegister(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public ChannelRegister channelRegister(MessagingSystemClient client) {
        return new SimpleChannelRegister(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public MessagingSystemClient messagingSystemClient(
            WebClient.Builder webClientBuilder,
            MessagingSystemProperties properties
    ) {
        return new WebClientMessagingSystemClient(webClientBuilder, properties);
    }

    @Bean
    public PublisherInitializer publisherInitializer(
            ClientRegister clientRegister,
            ChannelRegister channelRegister,
            ChannelRegistration channelRegistration,
            SpringMessagingSystemPublisherProperties properties
    ) {
        return new PublisherInitializer(clientRegister, channelRegister, channelRegistration, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public <T> Publisher<T> publisher(ChannelRegistration channelRegistration, MessagingSystemClient client) {
        return new SimplePublisher<>(
                channelRegistration,
                client
        );
    }
}
