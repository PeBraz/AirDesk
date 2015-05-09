package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;
import java.util.List;

public class FindWorkspaceReplyMessage extends Message implements Serializable{

    private List<String> workspaces;

    public FindWorkspaceReplyMessage(List<String> workspaces) {
        super();
        this.workspaces = workspaces;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.FIND_WORKSPACE_REPLY;
    }

    public List<String> getWorkspaces(){
        return this.workspaces;
    }
}
