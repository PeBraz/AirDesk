package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MessageType;

public class FindWorkspaceMessage extends Message {

    public String query;

    public FindWorkspaceMessage(String query) {
        super();
        this.query = query;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.FIND_WORKSPACE;
    }
}
