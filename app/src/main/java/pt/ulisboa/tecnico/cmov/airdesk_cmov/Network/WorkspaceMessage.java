package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class WorkspaceMessage extends Message implements Serializable{

    public List<String> workspaces;
    public WorkspaceMessage(List<String> workspaces, String email) {
        super(MessageType.WORKSPACE_INFO, email);
        this.workspaces = new ArrayList(workspaces);
    }
}
