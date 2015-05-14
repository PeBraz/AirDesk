package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class LockReadFileMessageReply extends Message implements Serializable{

    private final String email;
    private final String text;
    private final String key;

    public LockReadFileMessageReply(String email, String text, String key) {
        this.email = email;
        this.text = text;
        this.key = key;
    }


    public String getKey() {
        return key;
    }
    public String getEmail() {
        return email;
}
    public String getText() {
        return text;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.LOCK_READ_FILE_MESSAGE_REPLY;
    }
}
