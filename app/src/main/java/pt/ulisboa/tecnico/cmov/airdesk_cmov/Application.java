package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import android.os.Environment;
import android.os.StatFs;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.FilesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserNotFoundException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class Application {


    public static SessionManager session = null;

    private static ApplicationOwner owner = null;

    private static UsersDataSource userData = null;

    private static FilesDataSource fileData = null;

    //The Data source should not be called from here, should instead be called from the owner
    private static WorkspacesDataSource workspaceData = null;
    public static final int MAX_APPLICATION_QUOTA = 20; //do something with this

    public static final Set<Workspace> foreignWorkspaces = new HashSet<Workspace>();

    public static void init(android.content.Context AppContext) {
        Application.setUsersDataSource(new UsersDataSource(AppContext));
        Application.setWorkspacesDataSource(new WorkspacesDataSource(AppContext));
        Application.setFileDataSource(new FilesDataSource(AppContext));
        Application.session = new SessionManager(AppContext);
    }


    public static void createUser(String username, String email)
        throws NoDatabaseException, UserAlreadyExistsException {

        if (Application.userData == null) throw new NoDatabaseException();

        User u = new User(username, email);
        boolean isAllowed = checkUser(u);

        if (!isAllowed) userData.create(u);

        else throw new UserAlreadyExistsException();
    }

    public static void setFileDataSource(FilesDataSource filedb) {Application.fileData = filedb;}
    public static void setUsersDataSource(UsersDataSource userdb) {
        Application.userData = userdb;
    }
    public static void setWorkspacesDataSource(WorkspacesDataSource workspacedb) {
        Application.workspaceData = workspacedb;
    }

    public static ApplicationOwner getOwner() {
        if (Application.owner == null)
            throw new ApplicationHasNoUserException();

        return Application.owner;
    }

    public Application (User u) {
        Application.owner = ApplicationOwner.fromUser(u);
    }

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());

        return user != null;
    }

    public static void login(String email)
          throws NotRegisteredException, NoDatabaseException, WrongPasswordException{

        if (userData == null) throw new NoDatabaseException();

        User u = userData.get(email);
        if (u == null)throw new NotRegisteredException();

        Application.owner = ApplicationOwner.fromUser(u);

    }

    public static User getUser(String email){
        return userData.get(email);
    }


    /**
     * Tries to find a user in the network with a specific email
     *
     * @param email unique identifier for the user in the local network
     */
    public static User getNetworkUser(String email) throws UserNotFoundException{
        for (User u : Application.userData.getAll())
            if (u.getEmail().equals(email))
                return u;
        throw new UserNotFoundException(email);
    }

    /**
     * Local method to simulate the workspaces that are visible (public) in the network
     *
     * @return all public workspaces
     */

    public static Set<Workspace> getPublicNetworkWS() {
        final Set<Workspace> publicWS = new HashSet<>();
        for (Workspace ws : Application.workspaceData.getAll()) {
            if (!ws.getPrivacy())
                publicWS.add(ws);
        }
        return publicWS;
    }

    /**
     * Method used to simulate all workspaces in the network, users have access to private workspaces
     * if they were invited or subscribed while public
     *
     * @return all workspaces in the current network
     */
    public static Set<Workspace> getAllNetworkWS() {
        return  new HashSet<>(Application.workspaceData.getAll());
    }

    /**
     * Will search for workspaces available in the network
     *  (don't confuse workspacesInNetwork with foreignWorkspace)
     *
     */
    public static Set<Workspace> networkSearch(String query) {
        String[] queryArr = query.split("\\s+");
        Set<Workspace> availableWS = new HashSet<>();

        //By default providing no arguments to the query will show all available workspaces
        if (query.trim().isEmpty())
            return new HashSet<Workspace>(Application.getPublicNetworkWS());

        for (Workspace ws : Application.getPublicNetworkWS()) {

            //Dont' allow owned workspaces to appear
            if (ws.populateUser().getOwner().getEmail().equals(Application.getOwner().getEmail()))
                continue;

            for (String tag : ws.getTagsAsArray()) {
                for (String q: queryArr) {
                    if (q.equals(tag)) {
                        availableWS.add(ws);
                    }
                }
            }
        }
        return availableWS;
    }

    public static void subscribe(Set<Workspace> targetWs) {
           // Application.foreignWorkspaces.addAll(targetWs);
        for (Workspace ws: targetWs)
            Application.getOwner().addForeign(ws);
    }
    /**
    * Returns the device storage available in the internal storage of the device (as bytes)
    * gives no information on how the space limited by the quota values
    *
    *  (add better functionality change this)
    */
    public static int getDeviceStorageSpace() {
        //int OneMB = 1024 * 1024;
        return  Application.MAX_APPLICATION_QUOTA;
       // StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        //return (int) ((long)stat.getBlockSize() * (long)stat.getBlockCount() /(long) oneMB);
    }

    /**
     * Saves a modified user in the application
     * @param u user to be changed
     */
    public static void saveUser(User u) {
        Application.userData.save(u);
    }

    public static void remove(Workspace ws){
        Application.workspaceData.remove(ws.getName(), ws.populateUser().getOwner().getEmail());
    }

}
