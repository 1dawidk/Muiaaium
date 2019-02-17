package pl.dawidkulpa.muiaaium;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recording> recordings;
    private Recorder recorder;
    private Player player;

    //Views
    private Button recordBtn;
    private ProgressBar progressBar;

    private boolean isRecording;

    private RecordingsListAdapter rla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRecordingPermission();

        recordings= new ArrayList<>();
        Recording.loadFromFile("datafile.rcg", recordings);

        rla= new RecordingsListAdapter(this, recordings);
        ListView listView= findViewById(R.id.recordings_list);
        listView.setAdapter(rla);

        recordBtn= findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecordClick();
            }
        });

        findViewById(R.id.play_real_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayHumanClick();
            }
        });

        recorder= new Recorder(44100);
        player= new Player(44100);

        isRecording= false;

        progressBar= findViewById(R.id.progressbar);
    }

    private void getRecordingPermission() {
        String[] perms;
        boolean granted;

        //Check if already granted
        granted= ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;

        if (!granted) {
            perms= new String[]{
                    Manifest.permission.RECORD_AUDIO};

            ActivityCompat.requestPermissions(this, perms, 1);
        }
    }

    private void onRecordClick(){
        if(isRecording){
            recordBtn.setText(R.string.btn_record);
            recorder.stop();
            isRecording=false;

            //TODO: Get recorded buffer
            recordings.add(new Recording(recorder.getAudioBuffer(), recorder.getAudioBufferSize(), recorder.getSampleRate()));
            rla.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        } else {
            recordBtn.setText(R.string.btn_stop_record);
            recorder.start();
            isRecording=true;
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void onPlayHumanClick(){
        if(player.isPlaying()){
            player.stop();
        } else {
            int selIdx= rla.getSelectedItem();
            if(selIdx>-1)
                player.start(recordings.get(selIdx).getRawAudio());
            else
                Toast.makeText(this, "Wybierz nagranie", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPlayCatClick(){
        if(player.isPlaying()){
            player.stop();
        } else {
            player.start(null);
        }
    }
}
