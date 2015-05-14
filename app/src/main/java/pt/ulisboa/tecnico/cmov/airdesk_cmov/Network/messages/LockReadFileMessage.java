package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;

import java.io.Serializable;


public class LockReadFileMessage extends Message implements Serializable{

    private String wsname;
    private String filename;

    public LockReadFileMessage(String wsname, String filename){
        this.wsname = wsname;
        this.filename = filename;
    }

    public String getWsname(){
        return this.wsname;
    }
    public String getFilename(){
        return this.filename;
    }
    @Override
    public MessageType getMessageType(){
        return MessageType.LOCK_READ_FILE_MESSAGE;
    }
}
