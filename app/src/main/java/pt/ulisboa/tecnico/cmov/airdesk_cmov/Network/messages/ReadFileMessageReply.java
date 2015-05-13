package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


import java.io.Serializable;

public class ReadFileMessageReply extends Message implements Serializable {


    private String email;
    private String text;

    public ReadFileMessageReply(String email, String text) {
        this.email = email;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public MessageType getMessageType(){
        return MessageType.READ_FILE_MESSAGE_REPLY;
    }
}
