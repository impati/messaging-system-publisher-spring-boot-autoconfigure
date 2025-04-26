package com.example.impati.autoconfigure;

import com.example.impati.messaging_system_publisher.core.ChannelRegister;
import com.example.impati.messaging_system_publisher.core.ChannelRegistration;
import com.example.impati.messaging_system_publisher.core.ClientRegister;
import jakarta.annotation.PostConstruct;

public class PublisherInitializer {

    private final ClientRegister clientRegister;
    private final ChannelRegister channelRegister;
    private final ChannelRegistration channelRegistration;
    private final SpringMessagingSystemPublisherProperties properties;

    public PublisherInitializer(ClientRegister clientRegister,
                                ChannelRegister channelRegister,
                                ChannelRegistration channelRegistration,
                                SpringMessagingSystemPublisherProperties properties
    ) {
        this.clientRegister = clientRegister;
        this.channelRegister = channelRegister;
        this.channelRegistration = channelRegistration;
        this.properties = properties;
    }

    @PostConstruct
    void init() {
        clientRegister.register(properties.clientName());
        channelRegister.register(channelRegistration.getChannels().values().stream().toList());
    }
}
