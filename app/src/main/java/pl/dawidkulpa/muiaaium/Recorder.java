package pl.dawidkulpa.muiaaium;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;

public class Recorder {
    private int sampleRate;

    private Thread recordingThread;
    private boolean running;

    private short audioBuffer[];
    private int recordingLen;

    public Recorder(int sampleRate){
        this.sampleRate= sampleRate;
        running= false;
    }

    public void start(){
        running= true;

        recordingThread= new Thread(new Runnable() {
            @Override
            public void run() {

                running=true;
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

                int bufferSize= AudioRecord.getMinBufferSize(sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                    bufferSize = sampleRate * 2;
                }

                audioBuffer = new short[sampleRate*60];

                AudioRecord record= new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                        sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

                if (record.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.e("Recorder", "Audio Record can't initialize!");
                    return;
                }

                record.startRecording();

                Log.v("Recorder", "Start recording");

                recordingLen=0;
                while (running){
                    int numberOfShort = record.read(audioBuffer, recordingLen, (bufferSize/2));
                    recordingLen += numberOfShort;
                }

                record.stop();
                record.release();

                Log.v("Recorder", String.format("Recording stopped. Samples read: %d", recordingLen));
            }
        });

        recordingThread.start();
    }

    public void stop(){
        running= false;
    }

    public short[] getAudioBuffer(){
        return audioBuffer;
    }

    public int getAudioBufferSize(){
        return recordingLen;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
