package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;


public class LoginActivity extends ActionBarActivity {

    private UsersDataSource datasource;

    private List<String> allUsers;

    private EditText username = null;
    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        datasource = new UsersDataSource(this);
        datasource.open();

        username = (EditText)findViewById(R.id.editText2);
        button = (Button)findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allUsers = datasource.getAllUsers();

                if(allUsers.contains(username.getText().toString())){

                    Intent intent = new Intent(LoginActivity.this, WorkSpacesActivity.class);
                    intent.putExtra("location", "You are logged in as " + username.getText().toString());
                    startActivity(intent);

                }
            }
        });

    }

}
