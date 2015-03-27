package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class RegisterActivity extends ActionBarActivity {

    private EditText username = null;
    private EditText email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Application.setUsersDataSource(new UsersDataSource(this));

        username = (EditText)findViewById(R.id.editText2);
        email = (EditText)findViewById(R.id.editText);

        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (("".equals(username.getText().toString().trim()) || "".equals(email.getText().toString().trim()))){
                    Toast.makeText(RegisterActivity.this, "A value is missing!", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Application.createUser(username.getText().toString(),email.getText().toString());
                    Application.login(email.getText().toString());
                    Application.session = new SessionManager(getApplicationContext());
                    Application.session.createLoginSession(email.getText().toString(),username.getText().toString());
                    Toast.makeText(RegisterActivity.this, "Welcome to AirDesk.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, WorkSpacesActivity.class);
                    startActivity(intent);
                } catch (UserAlreadyExistsException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (WrongPasswordException e) {
                    e.printStackTrace();
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }


            }
        });
    }

}
