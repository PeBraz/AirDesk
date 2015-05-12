package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;

public class Peer {

    public Map<String, String[]> getWorkspaces() {
        return workspaces;
    }

   
    private Map<String, String[]> workspaces = new HashMap<>();

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
}
