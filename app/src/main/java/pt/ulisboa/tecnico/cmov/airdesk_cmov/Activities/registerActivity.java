package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;


public class registerActivity extends ActionBarActivity {

    private UsersDataSource datasource;

    private List<User> allUsers;

    private EditText username = null;
    private EditText email = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        datasource = new UsersDataSource(this);
        datasource.open();

        username = (EditText)findViewById(R.id.editText2);
        email = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText3);

        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (("".equals(username.getText().toString().trim()) || "".equals(email.getText().toString().trim())) || "".equals(password.getText().toString().trim())){
                    Toast.makeText(registerActivity.this, "A value is missing!", Toast.LENGTH_LONG).show();
                    return;
                }

                User u = new User(username.getText().toString(),password.getText().toString(),email.getText().toString());

                boolean isAllowed = checkUser(u);

                if(!isAllowed){

                    datasource.createUser(u);
                    Toast.makeText(registerActivity.this, "User created. Please sign in.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                else Toast.makeText(registerActivity.this, "Email or username already exists. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkUser(User user){

        boolean flag = false;

        allUsers = datasource.getAllUsers();

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
}
