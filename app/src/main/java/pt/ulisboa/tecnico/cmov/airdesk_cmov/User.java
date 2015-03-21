package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;

public final class User {

    private final WorkspacesDataSource workspacedb;
    private String username;
    private String email;


    public User(final String username, final String email) {
        this.username = username;
        this.email = email;
        this.workspacedb = new WorkspacesDataSource();
    }

    public User(){
        this.workspacedb = new WorkspacesDataSource();
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



    public final void createWorkspace(final String name, final int quota)
            throws WorkspaceAlreadyExistsException, InvalidQuotaException {
        if (quota > 10) throw new InvalidQuotaException(quota);  // needs to be changed to mirror the device's real space
        if (this.workspacedb.get(name) != null) throw new WorkspaceAlreadyExistsException(name);
        this.workspacedb.create(new Workspace(name, quota, this));
    }
    public final Workspace getWorkspace(final String name) {
        return this.workspacedb.get(name);
    }

    public final void subscribe(Workspace workspace) {
        //TODO -> after foreign workspaces
    }

}
