package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


import java.io.Serializable;
import java.util.List;


public class FilesMessageReply  extends Message implements Serializable {

    private List<String> files;
    private String email;
    private String workspace;

    public FilesMessageReply(String email,String workspace, List<String> files) {
        super();
        this.email = email;
        this.workspace = workspace;
        this.files = files;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.FILES_MESSAGE_REPLY;
    }


    public String getWorkspace() {
        return workspace;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getFiles() {
        return files;
    }


}
