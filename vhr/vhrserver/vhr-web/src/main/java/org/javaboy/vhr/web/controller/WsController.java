package org.javaboy.vhr.web.controller;

import org.javaboy.vhr.web.model.ChatMsg;
import org.javaboy.vhr.web.model.Hr;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

@Controller
public class WsController {
    // 这是Websocket提供的一种消息发送的模板
    @Resource
    SimpMessagingTemplate simpMessagingTemplate;

    /*
     * 一对一发送的前缀
     * 订阅主题：/user/queue/chat
     * 推送方式：1、@SendToUser("/demo3/greetings")
     *          2、messagingTemplate.convertAndSendToUser(destUsername, "/queue/chat", chatMsg);
     */

    // 我们不能发送消息时，直接传入当前发送者的用户名等信息，容易被冒名顶替，因此我们使用登录后的登录凭证来进行判断
    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        Hr hr = (Hr) authentication.getPrincipal();
        chatMsg.setFrom(hr.getUsername());
        chatMsg.setFromNickname(hr.getName());
        chatMsg.setDate(new Date());
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}
