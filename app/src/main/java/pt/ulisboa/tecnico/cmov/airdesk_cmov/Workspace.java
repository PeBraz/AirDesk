package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;

public class Workspace {

    private String name;
    private ArrayList<User> clients = new ArrayList<>();
    private User owner;
    private int maxQuota;
    private int minQuota;
    private boolean isPrivate;

    public int getMaxQuota() {
        return maxQuota;
    }

    public int getMinQuota() {
        return minQuota;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getClients() {
        return clients;
    }

    public void createFile(String name, String content){

        //updateQuota
    }
}
