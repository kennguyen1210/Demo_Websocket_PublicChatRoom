package ra.demowebsocket.model;

import lombok.Data;

@Data
public class Chat {
    private String message, sender;
    private MessageType type;
    public enum MessageType{
        JOIN,LEAVE, CHAT
    }
}
