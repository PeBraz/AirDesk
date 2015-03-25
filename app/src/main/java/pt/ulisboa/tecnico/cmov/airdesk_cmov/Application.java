package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class Application {

    private static User owner = null;
    public static SessionManager session = null;

    private List<Workspace> myWorkspaces;

    private static UsersDataSource userData = null;
    private static WorkspacesDataSource workspaceData = null;

    public static void init(android.content.Context AppContext) {
        Application.setUsersDataSource(new UsersDataSource(AppContext));
        Application.setWorkspacesDataSource(new WorkspacesDataSource(AppContext));
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

    public static void setUsersDataSource(UsersDataSource userdb) {
        Application.userData = userdb;
    }
    public static void setWorkspacesDataSource(WorkspacesDataSource workspacedb) {
        Application.workspaceData = workspacedb;
    }

    public static User getOwner() { return Application.owner; }

    public Application (User u) {
        Application.owner = u;
        this.myWorkspaces = new ArrayList<>();
    }

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());

        return user != null;
    }

    public static void login(String email) throws NotRegisteredException, WrongPasswordException {

        if (userData == null) throw new NoDatabaseException();

        User u = userData.get(email);
        if (u == null)throw new NotRegisteredException();

        Application.owner = userData.get(email);

    }

    public static List<Workspace> getMyWorkspaces(){

        List<Workspace> myWorkspaces = new ArrayList<>();
        List<Workspace> allWorkspaces = workspaceData.getAll();

        System.out.println(allWorkspaces.toString());

        for(Workspace work : allWorkspaces){
            if (work.getOwner().getEmail().equals(Application.getOwner().getEmail()))
                myWorkspaces.add(work);
        }

        return myWorkspaces;
    }

}
