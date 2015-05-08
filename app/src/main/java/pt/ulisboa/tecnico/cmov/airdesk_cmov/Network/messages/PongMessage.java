package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PongMessage extends Message implements Serializable{

    public List<InetAddress> allPeers = new ArrayList<>();

    public PongMessage(List<InetAddress> list){
        super();
        this.allPeers = list;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.PONG;
    }
}

