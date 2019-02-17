package pl.dawidkulpa.muiaaium;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.nio.ShortBuffer;

public class Player {
    private Thread playThread;
    private boolean isRunning;
    private int sampleRate;

    public Player(int sampleRate){
        this.sampleRate= sampleRate;
        isRunning=false;
    }

    public void start(final ShortBuffer samples){
        playThread= new Thread(new Runnable() {
            @Override
            public void run() {
                int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                    bufferSize = sampleRate * 2;
                }

                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize,
                        AudioTrack.MODE_STREAM);

                audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                    @Override
                    public void onMarkerReached(AudioTrack track) {

                    }

                    @Override
                    public void onPeriodicNotification(AudioTrack track) {

                    }
                });

                //audioTrack.setNotificationMarkerPosition(samples.limit());

                audioTrack.play();

                Log.v("Player", "Audio streaming started");

                short[] buffer = new short[bufferSize];
                samples.rewind();
                int limit = samples.limit();
                int totalWritten = 0;

                while (samples.position() < limit && isRunning) {
                    int numSamplesLeft = limit - samples.position();
                    int samplesToWrite;
                    if (numSamplesLeft >= buffer.length) {
                        samples.get(buffer);
                        samplesToWrite = buffer.length;
                    } else {
                        for (int i = numSamplesLeft; i < buffer.length; i++) {
                            buffer[i] = 0;
                        }
                        samples.get(buffer, 0, numSamplesLeft);
                        samplesToWrite = numSamplesLeft;
                    }
                    totalWritten += samplesToWrite;
                    audioTrack.write(buffer, 0, samplesToWrite);
                }

                if (!isRunning) {
                    audioTrack.release();
                }

                Log.v("Player", "Audio streaming finished. Samples written: " + totalWritten);
            }
        });

        isRunning= true;
        playThread.start();
    }

    public void stop(){
        isRunning=false;
    }

    public boolean isPlaying(){
        return isRunning;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
