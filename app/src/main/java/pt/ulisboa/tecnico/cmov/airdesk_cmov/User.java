package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;

public class User {

    private final WorkspacesDataSource workspacedb;
    private String username;
    private String email;
    private Set<WorkspaceDto> foreignWs;


    public User(final String username, final String email) {
        this.username = username;
        this.email = email;
        this.foreignWs = new HashSet<>();
        this.workspacedb = new WorkspacesDataSource();
    }

    public User(final String email) {
        this.username =  null;
        this.email = email;
        this.foreignWs = new HashSet<>();
        this.workspacedb = new WorkspacesDataSource();
    }



    public User(){
        this.workspacedb = new WorkspacesDataSource();
    }

    protected final WorkspacesDataSource getWorkspaceDataSource() {
        return this.workspacedb;
    }

    public final String getUsername() {
        return username;
    }

    public final String getEmail() { return email; }

    public final void setUsername(final String username) { this.username = username; }

    public final void setEmail(final String email) { this.email = email; }

    public final String toString() {
        return username;
    }

    public final Workspace getWorkspace(final String name) {
        return this.workspacedb.get(name, this.getEmail());
    }
    public final void saveWorkspace(final Workspace ws) {
        this.workspacedb.save(ws);
    }

    public final Set<WorkspaceDto> getForeign() {
        return this.foreignWs;
    }

    protected final void setForeign(Set<WorkspaceDto> foreign) {
        this.foreignWs = foreign;
    }

    public final void addForeign(String name, String wsName) {
        WorkspaceDto dto = new WorkspaceDto(name, wsName);
        this.foreignWs.add(dto);
        this.save();
    }

    public final void remForeign(String name, String wsName) {
        this.foreignWs.remove(new WorkspaceDto(name, wsName));
        this.save();
    }

    /**
     * Deserializes the foreign workspace set taken as bytes array
     *
     * @param foreignBytes
     */
    public final void setForeign(byte[] foreignBytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(foreignBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bais);
            this.foreignWs = (Set<WorkspaceDto>) in.readObject();
        }catch (StreamCorruptedException e){
            this.foreignWs = new HashSet<>();
        }catch(IOException | ClassNotFoundException  e) {
            System.out.println(e.getMessage());
            this.foreignWs = new HashSet<>();
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
     * Serializes the foreign workspace set so that it can be serialized
     *
     * @return byte array of the foreign workspaces serialized
     */
    public final byte[] getForeignSerialized() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        byte[] bytes = null;
        try {
            oo = new ObjectOutputStream(baos);
            oo.writeObject(this.foreignWs);
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
     * Utility method for saving the user itself
     */
    public final void save() {
        Application.saveUser(this);
    }


}
