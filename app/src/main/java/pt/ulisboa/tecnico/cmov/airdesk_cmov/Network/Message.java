package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;



import java.io.Serializable;
import java.util.Random;


public class Message implements Serializable{

    public int id;
    public MessageType mType;
    public String email;


    public Message(MessageType mType, String email){
        this.mType = mType;
        this.id = new Random().nextInt();
        this.email = email;
    }
    public MessageType getMessageType(){
        return this.mType;
    }


}