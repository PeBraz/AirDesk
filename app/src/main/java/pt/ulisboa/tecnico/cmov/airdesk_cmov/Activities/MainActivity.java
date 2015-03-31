package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class MainActivity extends ActionBarActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application.init(getApplicationContext());

        if (Application.session.isLoggedIn()) {
            String email = Application.session.getUserInfo().get(SessionManager.KEY_EMAIL);
            try {
                Application.login(email);
                startActivity(new Intent(this, MyWorkSpacesActivity.class));
            }catch (NotRegisteredException | WrongPasswordException e) {
               System.out.println(e.getMessage());
            }
        }

        Button button = (Button) findViewById(R.id.button6);
        Button button2 = (Button) findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
        });

        button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);

            }
            });

    }


}
