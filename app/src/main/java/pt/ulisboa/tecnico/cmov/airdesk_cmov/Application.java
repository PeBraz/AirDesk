package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoUserDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;



public class Application {

    private User owner;
    private List<Workspace> myWorkspaces;
   // private static ArrayList<Workspace> myWorkspaces; -> this will come from the database
    // private static ArrayList<Workspace> foreignWorkspaces;

    private static UsersDataSource dataSource = null;

    public static void createUser(String username, String email)
        throws NoUserDatabaseException, UserAlreadyExistsException {

        if (Application.dataSource == null) throw new NoUserDatabaseException();

        User u = new User(username, email);
        boolean isAllowed = checkUser(u);

        if (!isAllowed) dataSource.createUser(u);

        else throw new UserAlreadyExistsException();
    }

    public static void setUsersDataSource(UsersDataSource userdb) {
        Application.dataSource = userdb;
        Application.dataSource.open();
    }



    public Application (User u) {
        this.owner = u;//find user     //this.owner = Application.getUser(username)
        this.myWorkspaces = new ArrayList<Workspace>();
    }



    public void createWorkSpace(String name, int quota, boolean isPrivate, List<String> tags) throws ApplicationHasNoUserException{
        if (owner == null)
            throw new ApplicationHasNoUserException();

        //this.workspace.add(name, quota, isPrivate, tags);

    }

    private static boolean checkUser(User user){

        boolean flag = false;

        List<User> allUsers = dataSource.getAllUsers();

        if (!allUsers.isEmpty()) {

            for (User u : allUsers) {

                if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {

                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean login(String username) throws NotRegisteredException, WrongPasswordException {

        List<User> allUsers = dataSource.getAllUsers();

        for(User user : allUsers){

            if (user.getEmail().equals(username))
                return true;

            if (user.getEmail().equals(username))
                throw new WrongPasswordException();
        }

        throw new NotRegisteredException();
    }

    public static List<Workspace> getMyWorkspaces(){
        List<Workspace> workspaces = new ArrayList<>();
        return workspaces;

    }
}
