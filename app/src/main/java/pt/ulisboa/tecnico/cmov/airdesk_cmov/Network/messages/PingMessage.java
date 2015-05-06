package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

public class PingMessage extends Message{

    public String email;

    public PingMessage(String mail){
        super();
        this.email = mail;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.PING;
    }
}
