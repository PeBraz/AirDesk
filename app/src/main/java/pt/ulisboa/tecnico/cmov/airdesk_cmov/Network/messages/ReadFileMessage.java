package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


import java.io.Serializable;

public class ReadFileMessage extends Message implements Serializable{

    private String wsname;
    private String filename;
    public ReadFileMessage(String wsname, String filename){
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
        return MessageType.READ_FILE_MESSAGE;
    }
}
