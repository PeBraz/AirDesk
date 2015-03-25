package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;

public class MyWorkSpacesActivity extends ActionBarActivity {

    private List<String> ws = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_spaces);

        List<Workspace> myWorkspaces = Application.getMyWorkspaces();
        ListView listview = (ListView) findViewById(R.id.listView);

        for(Workspace w : myWorkspaces) ws.add(w.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ws);
        listview.setAdapter(adapter);


    }

}




