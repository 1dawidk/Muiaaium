package pl.dawidkulpa.muiaaium;

import android.util.Log;

import java.nio.ShortBuffer;
import java.util.ArrayList;



public class Recording {
    private short rawAudio[];
    private short catAudio[];
    private int sampleRate;
    private String name;
    private int length;    //in milliseconds



    public Recording(String name, int length){
        this.name= name;
        this.length= length;
    }

    public Recording(short buffer[], int buffSize, int sampleRate){
        rawAudio= new short[buffSize];
        catAudio= new short[buffSize];
        System.arraycopy(buffer, 0, rawAudio, 0, buffSize);
        this.name= "Unknown";
        this.length= (int)(buffSize/ ((float)sampleRate/1000));
        this.sampleRate= sampleRate;

        createCatAudio();

        Log.v("Recording", "New: length: "+length+"ms, title: "+name);
    }

    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }

    public ShortBuffer getRawAudio(){
        return ShortBuffer.wrap(rawAudio);
    }

    public static void loadFromFile(String file, ArrayList<Recording> list){
        list.add(new Recording("Test1", 150000));
        list.add(new Recording("Test2", 150000));
    }

    private void createCatAudio(){
        int fftSize= sampleRate/100;
        int samplesNo= rawAudio.length;
        int partsNo= rawAudio.length/fftSize;

        ArrayList<double[]> spectrums= new ArrayList<>();

        double vectorReal[]= new double[fftSize];
        double vectorImg[]=  new double[fftSize];


        for (int p=0; p<partsNo; p++){
            //Get 10ms part of samples
            for(int i=0; i<fftSize; i++){
                vectorReal[i]= (double)rawAudio[(p*fftSize)+i];
                vectorImg[i]= 0;
            }

            //Get spectrum of 10ms part
            FFT.transform(vectorReal, vectorImg);

            //Save part spectrum
            spectrums.add(new double[fftSize/2]);
            for(int i=0; i<fftSize/2; i++){
                spectrums.get(p)[i]= Math.sqrt((vectorReal[i]*vectorReal[i]) + (vectorImg[i]+vectorImg[i]));
                if(p==0)
                    Log.e("bin "+i, "Radius: "+spectrums.get(p)[i]);
            }
        }

        //TODO: Divide all frequencies by 2
        //TODO: Create cat audio samples

        for(int p=0; p<partsNo; p++){
            for(int i=0; i<1; i++){

            }
        }
    }
}
