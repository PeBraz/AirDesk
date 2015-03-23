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
<<<<<<< HEAD
    public static SessionManager session = null;

=======
    private List<Workspace> myWorkspaces;
>>>>>>> ce1b796f344112926352a2345cfcc4821e1bf965

    private List<Workspace> myWorkspaces;
    private static UsersDataSource userData = null;

<<<<<<< HEAD
    public static void init(android.content.Context AppContext) {
        Application.setUsersDataSource(new UsersDataSource(AppContext));
        Application.session = new SessionManager(AppContext);
    }

=======
>>>>>>> ce1b796f344112926352a2345cfcc4821e1bf965
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
        Application.owner = u;
        this.myWorkspaces = new ArrayList<>();
    }

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());

        return user != null;
    }

    public static void login(String email) throws NotRegisteredException, WrongPasswordException {

<<<<<<< HEAD
        if (userData == null) throw new NoDatabaseException();

        User u = userData.get(email);
        if (u == null)throw new NotRegisteredException();

        Application.owner = userData.get(email);
=======
        User user = userData.get(email);
        if (user == null) throw new NotRegisteredException();
        Application.owner = user;

>>>>>>> ce1b796f344112926352a2345cfcc4821e1bf965
    }

    public static List<Workspace> getMyWorkspaces(){
        List<Workspace> workspaces = new ArrayList<>();
        return workspaces;

    }
}
