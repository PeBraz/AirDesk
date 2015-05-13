package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;

public class CreateFileMessage  extends Message implements Serializable {

    private String title;
    private String workspace;

    public CreateFileMessage(String workspace, String title) {
        super();
        this.workspace = workspace;
        this.title = title;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.CREATE_FILE_MESSAGE;
    }


    public String getWorkspace() {
        return workspace;
    }

    public String getTitle() {
        return title;
    }


}
