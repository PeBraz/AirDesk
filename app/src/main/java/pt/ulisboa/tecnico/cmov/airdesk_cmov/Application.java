package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class Application {

    private static User owner = null;
    private List<Workspace> myWorkspaces;
   // private static ArrayList<Workspace> myWorkspaces; -> this will come from the database
    // private static ArrayList<Workspace> foreignWorkspaces;

    private static UsersDataSource userData = null;

    private static SessionManager session;

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

    public static User getOwner() { return Application.owner; }

    public Application (User u) {
        Application.owner = u;//find user     //this.owner = Application.getUser(username)
        this.myWorkspaces = new ArrayList<Workspace>();
    }
/*  User does this - delete stuff later
    public void createWorkSpace(String name, int quota, boolean isPrivate, List<String> tags) throws ApplicationHasNoUserException{
        //if (owner == null)    -> this is stup
          //  throw new ApplicationHasNoUserException();

        //this.workspace.add(name, quota, isPrivate, tags);

    }*/

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());

        return user != null;
    }

    public static void login(String email) throws NotRegisteredException, WrongPasswordException {

        List<User> allUsers = userData.getAll();

        for(User user : allUsers) {
            if (user.getEmail().equals(email)) {
                Application.owner = user;
                return;
            }
        }
        throw new NotRegisteredException();
    }

    public static List<Workspace> getMyWorkspaces(){
        List<Workspace> workspaces = new ArrayList<>();
        return workspaces;

    }
}
