package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;

public class User {

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
        return this.workspacedb.get(name);
    }


}
