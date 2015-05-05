package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FileMessage extends Message implements Serializable{

    public List<String> files;
    public String workspaceName;

    public FileMessage(List<String> workspaces, String workspaceName, String email) {
        super(MessageType.FILE_INFO, email);
        this.workspaceName = workspaceName;
        this.files = new ArrayList(workspaces);
    }
}
