package com.myhouse.MyHouse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

//@Configuration
//@EnableWebSocketMessageBroker
//@ComponentScan("com.myhouse.MyHouse.*")
public class WebSocketConfig { //extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages.simpDestMatchers("/ws/**").authenticated(); // Apply authentication to WebSocket paths
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/alarm/real-estate");
//        config.setApplicationDestinationPrefixes("/app");
//        config.setUserDestinationPrefix("/alarm/real-estate");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws");
//        registry.addEndpoint("/ws").withSockJS();
//    }
}

