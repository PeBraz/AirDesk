package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FilesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.ReadFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.WorkspaceDto;

public class Peer {

    private boolean fileschanged = false;
    private boolean fileBodyChanged = false;
    private String fileBody;

    public Map<String, String[]> getWorkspaces() {
        return workspaces;
    }


    private Map<String, String[]> workspaces = new HashMap<>();
    private final Map<String, List<String>> files = new HashMap<>();
    private Socket s;

    public String getOwner() {
        return owner;
    }

    public Peer(String email, Socket s){
        System.out.println("created peer " + email);
        this.owner = email;
        this.s = s;
    }

    private String owner;

    public Peer(Map<String, String[]> workspaces, String owner){
        this.workspaces = workspaces;
        this.owner = owner;
    }

    public void addTags(Map<String, String[]> ws){
        for (Map.Entry<String, String[]> ola : ws.entrySet()){
            System.out.println("KARANNNNNN: " + ola.getKey());
        }
        this.workspaces = ws;
    }

    public void send(Message msg){
        try {
            ServerThread.send(msg,this.s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public WorkspaceDto getWorkspace(String wsName) {
        return ;
    }*/
    /**
    *   Gets the file for the current Workspace
     *  Until it received the files, the flag filesChanged is false
    */
    public void getRemoteFiles(String wsname) {
        this.fileschanged = false;
        this.send(new FilesMessage(wsname));
    }

    public List<String> getLocalFiles(String wsname)
    {
        return this.files.get(wsname);
    }

    public void setFiles(String wsname, List<String> files){
        synchronized (this.files){
            this.fileschanged = true;
            this.files.put(wsname, files);
        }
    }

    public boolean filesChanged(){
        return fileschanged;
    }

    public void getRemoteFileBody(String wsname, String title){
        this.fileBodyChanged = false;
        this.send(new ReadFileMessage(wsname, title));
    }

    public String getLocalFileBody(){
        return this.fileBody;
    }
    public void setFileBody(String body){
        this.fileBody = body;
        this.fileBodyChanged = true;
    }
    public boolean fileBodyChanged(){
        return this.fileBodyChanged;
    }

}
