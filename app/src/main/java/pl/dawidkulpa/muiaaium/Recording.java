package pl.dawidkulpa.muiaaium;

import android.util.Log;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Recording {
    private short audio[];
    private String name;
    private int length;    //in milliseconds

    public Recording(String name, int length){
        this.name= name;
        this.length= length;
    }

    public Recording(short buffer[], int buffSize, int sampleRate){
        this.audio= buffer;
        this.name= "Unknown";
        this.length= (int)(buffSize/ ((float)sampleRate/1000));

        Log.v("Recording", "New: length: "+length+"ms, title: "+name);
    }

    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }

    public ShortBuffer getRawAudio(){
        return ShortBuffer.wrap(audio);
    }

    public static void loadFromFile(String file, ArrayList<Recording> list){
        list.add(new Recording("Test1", 150000));
        list.add(new Recording("Test2", 150000));
    }
}
