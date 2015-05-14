package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class InviteMessage extends Message implements Serializable{

    public String workspace;
    public String email;

    public InviteMessage(String email, String workspace){
        super();
        this.email = email; //the sender
        this.workspace = workspace; //the sender's workspace
    }

    public String getEmail() {
        return email;
    }

    public String getWorkspace() {
        return workspace;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.INVITE_MESSAGE;
    }
}
