package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class FindWorkspaceMessage extends Message implements Serializable{

    private String query;

    public FindWorkspaceMessage(String query) {
        super();
        this.query = query;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.FIND_WORKSPACE;
    }

    public String getQuery() {
        return query;
    }
}
