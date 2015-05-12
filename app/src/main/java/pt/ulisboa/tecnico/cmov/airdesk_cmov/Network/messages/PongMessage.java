package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PongMessage extends Message implements Serializable{


    public String getOwner() {
        return owner;
    }

    private String owner;

    public List<InetAddress> allPeers = new ArrayList<>();

    public PongMessage(List<InetAddress> list, String owner){
        super();
        this.allPeers = list;
        this.owner = owner;
    }



    @Override
    public MessageType getMessageType(){
        return MessageType.PONG;
    }
}

