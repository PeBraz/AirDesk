package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;


public class DeleteFileMessage extends Message implements Serializable{

    private final String workspace;
    private final String filename;

    public DeleteFileMessage(String workspace, String filename){
        this.workspace = workspace;
        this.filename = filename;
    }

    public String getWorkspace(){
        return this.workspace;
    }
    public String getFilename(){
        return this.filename;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.DELETE_FILE_MESSAGE;
    }
}
