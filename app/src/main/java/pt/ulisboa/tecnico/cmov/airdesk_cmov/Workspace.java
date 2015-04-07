package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import android.test.ApplicationTestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.FilesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotOwnerException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.StorageOverLimitException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyAddedException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserIsMyselfException;



public class Workspace {

    private String name;
    private ArrayList<User> clients = new ArrayList<>();

    /**
     *  Retrieved by the database when initializing this workspace, can be used to populate the user
     */
    private String ownerEmail;

    private User owner;
    private int quota;
    private int usedStorage;
    private boolean isPrivate;
    private String tags;
    private static FilesDataSource filedb;
    private List<String> accessList = new ArrayList<>();



    public Workspace(final String name,final int quota, final User owner) {
        this.name = name;
        this.quota = quota;
        this.owner = owner;
        this.isPrivate = true;
        this.usedStorage = 0;
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

    public final User getOwner() {
        return owner;
    }

    public final String getName() {
        return name;
    }

    public final boolean getPrivacy() { return isPrivate; }

    public final String getTags() { return tags; }

    public final int getStorage() {
        return this.usedStorage;
    }

    public final String[] getTagsAsArray() {
        return  (tags != null)? this.tags.split("\\s+"): new String[]{};
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

    public final void setOwnerEmail(String email) {
        this.ownerEmail = email;
    }

    public final void setStorage(int storage) {
        this.usedStorage = storage;
    }

    public static void createFile(String name, String workspace, String user){
        File file = new File(name,workspace, user);
        filedb.create(file);
    }

    public static List<String> getAllFiles(String workspace){

        ArrayList<String> allFiles = new ArrayList<>();

        List<File> listFiles =  filedb.getAll();
        for(File f : listFiles){
            if (f.getWorkspace().equals(workspace) && f.getUser().equals(Application.getOwner().getEmail()))
                allFiles.add(f.getName());
        }

        return allFiles;
    }

    /**
     * Changes the total amount of storage that this workspace uses. The new value must be between
     * 0 and the quota of the workspace.
     *
     * @param offset size of bytes added or removed from the text file
     */

    public void changeStorageUsed(int offset)
            throws StorageOverLimitException {
        int newStorageSpace = this.usedStorage + offset;

        if (newStorageSpace > quota || newStorageSpace < 0)
            throw new StorageOverLimitException(0, quota, newStorageSpace);

        this.usedStorage += offset;
        this.save();
    }


    public static void deleteFile(String fileName, String wsName, String ownerMail){

        filedb.delete(fileName,wsName,ownerMail);
    }

    public final void addAccessListUser(User u) {
        this.accessList.add(u.getEmail());
    }
    public final void remAccessListUser(User u) {
        for (String email: this.accessList)
            if (email.equals(u.getEmail()))
                this.accessList.remove(email);
    }

    /**
     * Returns the list of users stored in the access list by going to the database and getting all
     * This can't be used to perform changes on the access list.
     * @return list of users
     */
    public final List<User> getAccessList() {
        List<User> users = new ArrayList<>();
        for (String email: this.accessList)
            users.add(Application.getUser(email));
        return users;
    }

    /**
     * Deserializes the access list taken as bytes array
     *
     * @param accessBytes
     */
    public final void setAccessList(byte[] accessBytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(accessBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bais);
            this.accessList = (List<String>) in.readObject();
        }catch (StreamCorruptedException e){
            this.accessList = new ArrayList<>();
        }catch(IOException | ClassNotFoundException  e) {
            System.out.println(e.getMessage());
            this.accessList = new ArrayList<>();
        }finally {
            try{
                if (in != null) in.close();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            try{
                bais.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Serializes the Access User list so that it can be serialized
     *
     * @return byte array of the list of users
     */
    public final byte[] getAccessListSerialized() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        byte[] bytes = null;
        try {
            oo = new ObjectOutputStream(baos);
            oo.writeObject(this.accessList);
            bytes = baos.toByteArray();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }finally {
            try{
                if (oo != null) oo.close();
            }catch(IOException e) {
                System.out.println(e.getMessage());
            }
            try{
                baos.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }

        }
        return bytes;
    }

    /**
     * Checks if this workspace is the same as another
     *
     * @param o workspace to be compared with
     * @return if workspaces are the same or not
     */
    @Override
    public boolean equals(Object o) {
        return this.getName().equals(((Workspace) o).getName())
               && this.getOwner().getEmail().equals(
                ((Workspace) o).populateUser().getOwner().getEmail());
    }

    /**
     * Given this workspace, initialize the user by looking for it in the database,
     * populate user will load the user in the database by searching for the name.
     *
     * @return the object itself to allow chaining of methods
     */
    public final Workspace populateUser() {
        if (owner == null)
            this.owner = Application.getUser(this.ownerEmail);
        return this;
    }

    /**
     * Utility method for saving the workspace itself
     */
    public final void save() {
        this.populateUser().getOwner().saveWorkspace(this);
    }
    /**
     * Utility method for removing the workspace
     */
    public final void remove() {
        final User u = this.populateUser().getOwner();
        boolean notOwner = ! Application.getOwner().getEmail().equals(u.getEmail());

        if (notOwner)
               throw new NotOwnerException();
        Application.remove(this);
    }

    /**
     * Adds the user to the the application owner access list.
     *
     * Invites a foreign user to the workspace, attempting to connect to that foreign client
     * and requesting the invite.
     *
     * The provided user is confirmed to be available in the network.
     *
     * @param user that is invited
     */
    public final void invite(User user)
            throws UserIsMyselfException, UserAlreadyAddedException {

        if (user.getEmail().equals(Application.getOwner().getEmail()))
            throw new UserIsMyselfException();
        if (this.accessList.indexOf(user.getEmail()) != -1)
            throw new UserAlreadyAddedException(user.getEmail());

        this.addAccessListUser(user);
        this.save();
        /*
        *  The invite just stores it automatically into the target user foreign workspace list
        *
        */
        user.addForeign(this);
        user.save();
    }

    public final List<String> getFiles() {
        List<String> res = new ArrayList<>();
        for (File f : this.filedb.getAll())
            res.add(f.getName());
        return res;
    }
}
