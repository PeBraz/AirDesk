package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class PingMessage extends Message implements Serializable{

    @Override
    public MessageType getMessageType(){
        return MessageType.PING;
    }
}
