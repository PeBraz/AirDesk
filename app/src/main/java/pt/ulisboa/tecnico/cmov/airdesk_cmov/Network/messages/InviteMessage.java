package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.util.List;

/**
 * Created by pebraz on 06-05-2015.
 */
public class InviteMessage extends Message{

    public String workspace;
    public String email;
    public InviteMessage(String email, String workspace){
        super();
        this.email = email; //the sender
        this.workspace = workspace; //the sender's workspace
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.INVITE;
    }
}
