package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.FilesDataSource;

public class Workspace {

    private String name;
    private ArrayList<User> clients = new ArrayList<>();
    private User owner;
    private int quota;
    private int minQuota;
    private boolean isPrivate;
    private String tags;
    private FilesDataSource filedb;

    public Workspace(final String name,final int quota, final User owner) {
        this.name = name;
        this.quota = quota;
        this.owner = owner;
        this.isPrivate = true;
    }
    public Workspace(final String name, final int quota, final User owner,
                     final boolean privacy, final String tags) {
        this(name,quota,owner);
        this.isPrivate = privacy;
        this.tags = tags;
    }

    public Workspace () {this.filedb = new FilesDataSource();}

    public final int getQuota() {
        return quota;
    }

    public final int getMinQuota() {
        return minQuota;
    }

    public final User getOwner() {
        return owner;
    }

    public final String getName() {
        return name;
    }

    public final boolean getPrivacy() { return isPrivate; }

    public final String getTags() { return tags; }

    public final String[] getTagsAsArray() {
        return this.tags.split("\\s+");
    }

    public final ArrayList<User> getClients() {
        return clients;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final void  setQuota(final int maxQuota) {
        this.quota = maxQuota;
    }

    public final void setPrivacy (final boolean isPrivate) { this.isPrivate = isPrivate; }

    public final void setTags(final String tags) { this.tags = tags; }

    public final void invite(final User u) {
        //TODO
    }

    public void createFile(String name, String workspace){
        File file = new File(name,workspace);
        filedb.create(file);
    }

    public List<String> getAllFiles(String workspace){

        ArrayList<String> allFiles = new ArrayList<>();

        List<File> listFiles =  filedb.getAll();
        for(File f : listFiles){
            System.out.println(workspace);
            System.out.println(f.getWorkspace());
            if (f.getWorkspace().equals(workspace))
                allFiles.add(f.getName() + ".txt");
        }

        return allFiles;
    }

}
