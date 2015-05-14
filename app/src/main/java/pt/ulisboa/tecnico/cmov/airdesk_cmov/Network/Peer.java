package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.DeleteFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FilesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.InviteMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.LockReadFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.ReadFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.WriteFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.WorkspaceDto;

public class Peer {

    private boolean fileschanged = false;
    private boolean fileBodyChanged = false;
    private String fileBody;
    private Boolean lock = false;
    private final Object localLock = new Object();
    private String key = "";

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

    public boolean send(Message msg){
            return ServerThread.send(msg,this.s);
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
        String ret = this.fileBody;
        this.fileBody = "";
        return ret;
    }
    public void setFileBody(String body){
        this.fileBody = body;
        this.fileBodyChanged = true;
    }
    public boolean fileBodyChanged(){
        return this.fileBodyChanged;
    }

    /*
     * Check if a stored foreign workspace stopped being broadcasted by the peer
     */
    public boolean workspaceExists(String wsname){
        return this.workspaces.containsKey(wsname);
    }

    public void writeFile(String workspace, String filename, String text){
        this.send(new WriteFileMessage(workspace, filename, text, this.key));
    }
    public void deleteFile(String workspace, String filename){
        this.send(new DeleteFileMessage(workspace, filename));
    }


    public void getInvited(String workspaceName){
        this.send(new InviteMessage(Application.getOwner().getEmail(), workspaceName));
    }

    public boolean lockAcquired(){
        synchronized (this.localLock) {
            if (this.lock) {
                this.lock = false;
                return true;
            }
            return false;
        }
    }

    public void getLockedRemoteFileBody(String wsName, String title) {
        this.fileBodyChanged = false;
        this.send(new LockReadFileMessage(wsName, title));
    }

    //key being used to keep the current file locked
    public void setKey(String key, String text){
        synchronized (this.localLock) {
            this.lock = true;
            this.key = key;
            this.setFileBody(text);
        }
    }
}
