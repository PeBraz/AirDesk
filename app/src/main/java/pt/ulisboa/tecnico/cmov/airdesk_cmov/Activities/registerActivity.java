package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;


public class registerActivity extends ActionBarActivity {

    private UsersDataSource datasource;

    private EditText username = null;
    private EditText email = null;
    private EditText password = null;

    private User user = null;

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

                user = new User(username.getText().toString(), email.getText().toString(), password.getText().toString());
                datasource.createUser(user);

                Intent intent = new Intent(registerActivity.this, WorkSpacesActivity.class);
                intent.putExtra("location", "You are logged in as " + username.getText().toString());
                startActivity(intent);
            }
        });

    }


}
