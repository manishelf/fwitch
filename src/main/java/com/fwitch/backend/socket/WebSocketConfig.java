package com.fwitch.backend.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//https://github.com/manishelf/qtodo-spring-backend/tree/main/src/main/java/com/qtodo/socket
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{	
	
    @Autowired
    WebSocketSessionManager wsManager;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MediaSocketHandler(this.wsManager), "/fwitch/media");

        registry.addHandler(new ChatSocketHandler(this.wsManager), "/fwitch/chat");
    }
	
}