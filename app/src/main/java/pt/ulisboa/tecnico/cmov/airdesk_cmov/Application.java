package pt.ulisboa.tecnico.cmov.airdesk_cmov;



import java.util.ArrayList;
import java.util.List;


import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoUserDatabaseException;

public class Application {

    private User owner;
   // private static ArrayList<Workspace> myWorkspaces; -> this will come from the database
    // private static ArrayList<Workspace> foreignWorkspaces;

    private static UsersDataSource dataSource = null;

    public static void createUser(String username, String email, String password)
        throws NoUserDatabaseException{
        if (Application.dataSource == null) throw new NoUserDatabaseException();
        User u = new User(username,email,password);

        boolean isAllowed = checkUser(u);

        if(!isAllowed){

            dataSource.createUser(u);
/*            Toast.makeText(registerActivity.this, "User created. Please sign in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(registerActivity.this, MainActivity.class);
            startActivity(intent);*/
        }

/*        else Toast.makeText(registerActivity.this, "Email or username already exists. Try again.", Toast.LENGTH_SHORT).show();*/
    }

    public static void setUsersDataSource(UsersDataSource userdb) {
        Application.dataSource = userdb;
        Application.dataSource.open();
    }


    public Application (String username) {
        //find user     //this.owner = Application.getUser(username)
        //get workspace conn // this.myWorkspace = Application.getWorkspaces(username)

    }



    public void createWorkSpace(String name, int quota, boolean isPrivate, List<String> tags) throws ApplicationHasNoUserException{
        if (owner == null)
            throw new ApplicationHasNoUserException();

        this.workspace.add(name, quota, isPrivate, tags);

    }

    private static boolean checkUser(User user){

        boolean flag = false;

        allUsers = dataSource.getAllUsers();

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

    public static boolean login(){

        return false;
    }

    public static List<Workspace> getMyWorkspaces(){
        List<Workspace> workspaces = new ArrayList<>();
        return workspaces;
    }
}
