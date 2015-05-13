package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


        import java.io.Serializable;


public class FilesMessage  extends Message implements Serializable {

    private String workspaceName;

    public FilesMessage(String workspaceName) {
        super();
        this.workspaceName = workspaceName;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.FILES_MESSAGE;
    }

    public String getWorkspaceName(){
        return this.workspaceName;
    }
}
