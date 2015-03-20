package pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.MainActivity;

public class SessionManager {

    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";

    int PRIVATE_MODE = 0;

    SharedPreferences pref;
    Editor login_editor;
    Editor workspace_editor;
    Context context;

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        login_editor = pref.edit();
        workspace_editor = pref.edit();
        
    }

    public void createLoginSession(String email, String username){

        login_editor.putBoolean(IS_LOGIN, true);
        login_editor.putString(KEY_EMAIL, email);
        login_editor.putString(KEY_USERNAME, username);
        login_editor.commit();
    }

    public HashMap<String, String> getUserInfo(){

        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }

    public void checkLogin(){

        if(!this.isLoggedIn()){

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logoutUser(){

        login_editor.clear();
        login_editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
