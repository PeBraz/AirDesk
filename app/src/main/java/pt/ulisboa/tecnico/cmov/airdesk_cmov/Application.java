package pt.ulisboa.tecnico.cmov.airdesk_cmov;

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

    //maybe move this inside the user, right now this is temporary because the foreign
    // workspaces will work as dummy data
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

    public static ApplicationOwner getOwner() { return Application.owner; }

    public Application (User u) {
        Application.owner = (ApplicationOwner) u;
    }

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());

        return user != null;
    }

    public static void login(String email) throws NotRegisteredException, WrongPasswordException {

        if (userData == null) throw new NoDatabaseException();

        User u = userData.get(email);
        if (u == null)throw new NotRegisteredException();

        Application.owner = ApplicationOwner.fromUser(u);

    }

    public static User getUser(String email){
        return userData.get(email);
    }


    public static Set<Workspace> workspacesInNetwork = new HashSet<>();


    /**
     * Will search for workspaces available in the network
     *  (dont confuse workspacesInNetwork with foreigWworkspace)
     *
     */
    public static Set<Workspace> networkSearch(String query) {
        String[] queryArr = query.split("\\s+");
        Set<Workspace> availableWS = new HashSet<>();

        System.out.println("DEBUG:");
        System.out.println( "is empty:" + (query.trim().isEmpty()?"YES":"NO"));
        for (Workspace w : workspacesInNetwork)
            System.out.println(w.getName());
        if (query.trim().isEmpty())
            return new HashSet<Workspace>(workspacesInNetwork);

        for (Workspace ws : Application.workspacesInNetwork) {
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
            Application.foreignWorkspaces.addAll(targetWs);
    }

}
