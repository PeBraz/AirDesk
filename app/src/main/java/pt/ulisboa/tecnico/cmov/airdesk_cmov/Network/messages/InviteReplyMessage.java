package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

public class InviteReplyMessage extends Message{

    public String workspace;
    public String email;

    public InviteReplyMessage(InviteMessage msg){
        this.workspace = msg.workspace;
        this.email = msg.email;
    }

    public InviteReplyMessage(String email, String workspace){
        super();
        this.email = email;
        this.workspace = workspace;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.INVITE_REPLY;
    }
}
