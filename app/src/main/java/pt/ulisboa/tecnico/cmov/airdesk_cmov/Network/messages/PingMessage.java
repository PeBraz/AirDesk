package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class PingMessage extends Message implements Serializable{

    private String owner;

    public PingMessage(String name){
        this.owner = name;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.PING;
    }

    public String getOwner() {
        return owner;
    }
}
