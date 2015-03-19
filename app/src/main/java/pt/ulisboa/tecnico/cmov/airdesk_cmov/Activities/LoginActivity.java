package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class LoginActivity extends ActionBarActivity {

    private EditText username = null;
    private EditText password = null;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editText2);
        password = (EditText)findViewById(R.id.editText3);
        Button button = (Button) findViewById(R.id.button4);

        Application.setUsersDataSource(new UsersDataSource(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Application.login(username.getText().toString(),password.getText().toString());
                    session = new SessionManager(getApplicationContext());
                    session.createLoginSession(username.getText().toString());
                    Toast.makeText(LoginActivity.this, "Welcome to AirDesk.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, WorkSpacesActivity.class);
                    startActivity(intent);

                } catch (NotRegisteredException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (WrongPasswordException f) {
                    Toast.makeText(LoginActivity.this, f.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
