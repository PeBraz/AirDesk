package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.MainActivity;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.registerActivity;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;

public class Application {

    private static User owner;
    private static ArrayList<Workspace> myWorkspaces;
    private static ArrayList<Workspace> foreignWorkspaces;

    private static UsersDataSource dataSource;
    private static List<User> allUsers;

    public static void createUser(String username, String email, String password){

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

    public static void createWorkSpace(){
        //uss tem de estar logado

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
