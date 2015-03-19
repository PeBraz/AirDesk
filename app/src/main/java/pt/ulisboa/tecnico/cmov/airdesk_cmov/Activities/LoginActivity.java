package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;


public class LoginActivity extends ActionBarActivity {

    private UsersDataSource datasource;

    private List<User> allUsers;

    private EditText username = null;
    private EditText password = null;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        datasource = new UsersDataSource(this);
        datasource.open();

        username = (EditText)findViewById(R.id.editText2);
        password = (EditText)findViewById(R.id.editText3);
        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allUsers = datasource.getAllUsers();

                for (User user : allUsers) {

                    if (user.getUsername().equals(username.getText().toString()) && user.getPassword().equals(password.getText().toString())) {

                        session = new SessionManager(getApplicationContext());
                        session.createLoginSession(username.getText().toString());

                        Toast.makeText(LoginActivity.this, "Welcome to AirDesk.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, WorkSpacesActivity.class);
                        startActivity(intent);
                    }

                    else if (user.getUsername().equals(username.getText().toString()) && !user.getPassword().equals(password.getText().toString()))

                        Toast.makeText(LoginActivity.this, "Wrong password. Try again.", Toast.LENGTH_SHORT).show();

                    else Toast.makeText(LoginActivity.this, "Your are not registered yet, please sign up.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}