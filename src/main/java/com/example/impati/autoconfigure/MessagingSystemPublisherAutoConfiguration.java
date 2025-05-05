package com.example.impati.autoconfigure;

import com.example.impati.messaging_system_publisher.core.ChannelMessageRepository;
import com.example.impati.messaging_system_publisher.core.ChannelRegister;
import com.example.impati.messaging_system_publisher.core.ChannelRegistration;
import com.example.impati.messaging_system_publisher.core.ClientRegister;
import com.example.impati.messaging_system_publisher.core.MemoryChannelMessageRepository;
import com.example.impati.messaging_system_publisher.core.MessageDeliveryGuaranteePublisher;
import com.example.impati.messaging_system_publisher.core.MessagingSystemClient;
import com.example.impati.messaging_system_publisher.core.MessagingSystemPublisherProperties;
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
    public MessagingSystemPublisherProperties properties() {
        return new MessagingSystemPublisherProperties(properties.url(), properties.clientName(), properties.deliveryGuarantee());
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
            MessagingSystemPublisherProperties properties
    ) {
        return new WebClientMessagingSystemClient(webClientBuilder, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public <T> ChannelMessageRepository<T> channelMessageRepository(
            ChannelRegistration channelRegistration
    ) {
        return new MemoryChannelMessageRepository<>(
                channelRegistration
        );
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
    public <T> Publisher<T> publisher(
            ChannelRegistration channelRegistration,
            MessagingSystemClient client,
            WebClient.Builder webClientBuilder,
            MessagingSystemPublisherProperties properties,
            ChannelMessageRepository<T> channelMessageRepository
    ) {
        if (properties.deliveryGuarantee()) {
            return new MessageDeliveryGuaranteePublisher<>(channelRegistration, webClientBuilder, channelMessageRepository);
        }

        return new SimplePublisher<>(
                channelRegistration,
                client
        );
    }
}
