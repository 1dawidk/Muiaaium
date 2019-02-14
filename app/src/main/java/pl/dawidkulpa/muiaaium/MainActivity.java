package pl.dawidkulpa.muiaaium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recording> recordings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordings= new ArrayList<>();
        Recording.loadFromFile("datafile.rcg", recordings);

        RecordingsListAdapter rla= new RecordingsListAdapter(this, recordings);
        ListView listView= findViewById(R.id.recordings_list);
        listView.setAdapter(rla);
    }
}
