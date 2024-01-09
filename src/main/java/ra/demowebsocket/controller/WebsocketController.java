package ra.demowebsocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import ra.demowebsocket.model.Chat;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    // nhan tin nhan gui len tu stomp
    @MessageMapping("/chat/send-message")
    @SendTo("/topic/public-chat-room")
    public Chat sendChat(@Payload Chat chat){
        return chat;
    }

    // dang ki topic (vao phong chat)
    @MessageMapping("/chat/add-user")
    @SendTo("/topic/public-chat-room")
    public Chat addUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor){
        // them nguoi dung vao session
        headerAccessor.getSessionAttributes().put("username",chat.getSender());
        return chat;
    }
}
