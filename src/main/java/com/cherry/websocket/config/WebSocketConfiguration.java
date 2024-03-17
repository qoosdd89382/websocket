package com.cherry.websocket.config;

import com.cherry.websocket.channel.ChatChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter (){
        ServerEndpointExporter exporter = new ServerEndpointExporter();
        exporter.setAnnotatedEndpointClasses(ChatChannel.class);
        return exporter;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    }
}