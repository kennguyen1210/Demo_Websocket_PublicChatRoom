package ra.demowebsocket.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ra.demowebsocket.model.Chat;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketListener {
    private final SimpMessageSendingOperations simpMessage;
    @EventListener
    public void handleOpenedConnectionWebsocket(SessionConnectedEvent session){
        log.info("new connection to server websocket");
    }
    @EventListener
    public void handleClosedConnectionWebsocket(SessionDisconnectEvent session){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(session.getMessage());
        // lay ra username
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("Username : " + username + " is disconnected");
            Chat chat = new Chat();
            chat.setType(Chat.MessageType.LEAVE);
            chat.setSender(username);
            simpMessage.convertAndSend("/topic/public-chat-room",chat);
        }
    }
}
