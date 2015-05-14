package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;


public class WriteFileMessage extends Message implements Serializable {

    private final String text;
    private final String workspace;
    private final String filename;
    private final String key;

    public WriteFileMessage(String workspace, String filename, String text, String key){
        this.text = text;
        this.workspace = workspace;
        this.filename = filename;
        this.key = key;
    }

    public String getText(){return this.text;}
    public String getWorkspace(){return this.workspace;}
    public String getFilename(){return this.filename;}
    public String getKey(){ return this.key;}

    @Override
    public MessageType getMessageType() {
        return MessageType.WRITE_FILE_MESSAGE;
    }
}

