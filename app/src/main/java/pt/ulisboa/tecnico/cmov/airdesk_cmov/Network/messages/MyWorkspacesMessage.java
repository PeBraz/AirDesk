package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyWorkspacesMessage extends Message implements Serializable {

    private Map<String, String[]> workspaces;

    public String getOwner() {
        return owner;
    }

    private String owner;

    public MyWorkspacesMessage(Map<String, String[]> workspaces, String name) {
        super();
        this.workspaces = workspaces;
        this.owner = name;
    }

    @Override
    public MessageType getMessageType(){
        return MessageType.MY_WORKSPACES;
    }

    public Map<String, String[]> getWorkspaces(){
        return this.workspaces;
    }
}
