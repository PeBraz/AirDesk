package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WorkspaceAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.Peer;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceReplyMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MyWorkspacesMessage;


public final class ApplicationOwner extends User {

    /**
     * This constructor allows the creation of the application owner from an existing user that
     * was taken from the database without losing information
     * @param user taken from the database
     */
    public ApplicationOwner(User user) {
        super(user.getUsername(), user.getEmail());
        super.setForeign(user.getForeign());

    }

    public final void createWorkspace(final String name, final int quota)
            throws WorkspaceAlreadyExistsException, InvalidQuotaException {

        if (quota > Application.getDeviceStorageSpace()) throw new InvalidQuotaException(quota);  // needs to be changed to mirror the device's real space
        if (getWorkspaceDataSource().get(name, this.getEmail()) != null) throw new WorkspaceAlreadyExistsException(name);
        Workspace ws = new Workspace(name, quota, this);
        getWorkspaceDataSource().create(ws);
    }


    /**
     *  This method is used to create the application owner from an existing user that can be found
     *  in the database.
     *
     * @param u the user that will be turned into a ApplicationOwner
     * @return the application owner
     */
    public static ApplicationOwner fromUser(User u) {
        return new ApplicationOwner(u);
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

    /**
     *  Finds all workspaces in the network that the user has added
     *
     * @return set of workspaces that were found
     */
    public final Set<Workspace> getForeignWorkspaces() {
        Set<Workspace> foreign = new TreeSet<>();
        for (Workspace ws : Application.getAllNetworkWS()) {
            for (WorkspaceDto dto : super.getForeign()) {
                if (ws.getName().equals(dto.getWSName())
                    && ws.populateUser().getOwner().getEmail().equals(dto.getUserEmail()))
                    foreign.add(ws);
            }
        }
        return foreign;
    }
    public final Set<WorkspaceDto> getForeignWorkspacesAsDto() {
       return super.getForeign();
    }
    /**
     *  Removes the workspace from this user
     *  If the workspace belongs to this user, users in the access list need to be informed.
     *  If the workspace belongs to someone else, the owner needs to be informed.
     *
     * @param ws workspace to be removed
     */

    public final void remove(Workspace ws) {
    /*
        Removing the workspaces is performed locally, the real users are not informed,
        only their database information is updated
    */

        List<String> lista = new ArrayList<>();

        boolean isMyWs = Application.getOwner().getEmail().equals(ws.getOwner().getEmail());
        if (isMyWs) {
            ws.remove();
            }

        //check locally for subscription
    }

}


