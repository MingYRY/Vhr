package org.javaboy.vhr.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 协议都有哪些优势：
 * 由于 WebSocket 连接在端口80(ws)或者443(wss)上创建，与 HTTP 使用的端口相同，这样，基本上所有的防火墙都不会阻塞 WebSocket 连接
 * WebSocket 使用 HTTP 协议进行握手，因此它可以自然而然的集成到网络浏览器和 HTTP 服务器中
 * 使用该协议，当消息启动或者到达的时候，服务端和客户端都可以知道
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册stomp的端点，客户端通过连接点进行http握手连接
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ep").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置信息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
    }
}
