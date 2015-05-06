package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;
import java.util.Random;

public abstract class Message implements Serializable{

    public int id;

    public Message(){
        this.id = new Random().nextInt();
    }
    public abstract MessageType getMessageType();

}