package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;


public final class ApplicationOwner extends User {

    public ApplicationOwner() { super(); }
    public ApplicationOwner(String username, String email) { super(username, email);}


    public final void createWorkspace(final String name, final int quota)
            throws WorkspaceAlreadyExistsException, InvalidQuotaException {

        if (quota > Application.MAX_APPLICATION_QUOTA) throw new InvalidQuotaException(quota);  // needs to be changed to mirror the device's real space
        if (getWorkspaceDataSource().get(name, this.getEmail()) != null) throw new WorkspaceAlreadyExistsException(name);
        Workspace ws = new Workspace(name, quota, this);
        getWorkspaceDataSource().create(ws);
    }

    public final void subscribe(Workspace workspace) {
        Application.foreignWorkspaces.add(workspace);

    }

    /**
     *  This method is used to create the application owner from an existing user that can be found
     *  in the database.
     *
     * @param u the user that will be turned into a ApplicationOwner
     * @return the application owner
     */
    public static ApplicationOwner fromUser(User u) {
        return new ApplicationOwner(u.getUsername(),u.getEmail());
    }

    /**
     *  Checks the local workspaces stored in the database and returns those that belong
     *  to the current application owner
     *
     * @return the workspaces that belong to the current application owner
     */
    public final List<Workspace> getMyWorkspaces(){

        List<Workspace> myWorkspaces = new ArrayList<>();
        List<Workspace> allWorkspaces = super.getWorkspaceDataSource().getAll();

        for(Workspace work : allWorkspaces){
            if (work.populateUser().getOwner().getEmail().equals(Application.getOwner().getEmail()))
                myWorkspaces.add(work);
        }
        return myWorkspaces;
    }


}


