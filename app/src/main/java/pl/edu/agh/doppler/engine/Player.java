package pl.edu.agh.doppler.engine;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Our main tone player.
 * Based on answer in
 * <a href="http://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android">this</a>
 * SO question.
 */
public class Player {

    /**
     * Sampling frequency - 44,1kHz
     * @see <a href="http://pl.wikipedia.org/wiki/Próbkowanie">Wikipedia</a>
     */
    private static final int SAMPLE_RATE = 44100;

    /** Sample duration in s. */
    private static final int SAMPLE_DURATION = 5;

    /** Amount of samples. Calculated based on {@link #SAMPLE_RATE} and {@link #SAMPLE_DURATION}. */
    private static final int SAMPLES_NUM = SAMPLE_RATE * SAMPLE_DURATION;

    /** Object used to play our main tone. */
    private AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
            2*SAMPLES_NUM, AudioTrack.MODE_STATIC);

    /** Tone frequency in hz. */
    private double toneFrequency;

    /** Array for sampled tune after fft. */
    private byte[] generatedSound = new byte[2* SAMPLES_NUM];

    public Player(final double frequency) {
        setToneFrequency(frequency);
    }

    /** Sets new frequency and starts playing immediately. */
    public void changeToneFrequency(final double frequency) {
        setToneFrequency(frequency);
        play();
    }

    /** Sets tone frequency. This stops playing! */
    private void setToneFrequency(final double toneFrequency) {
        this.toneFrequency = toneFrequency;
        getTone();

        //set track to play
        audioTrack.write(generatedSound, 0, 2*SAMPLES_NUM);
        //set to loop for ever! :D
        audioTrack.setLoopPoints(0, SAMPLES_NUM/2, -1);
    }

    /** Start playing tone. */
    public void play() {
        Log.i("DOPPLER","Start playing");
        audioTrack.play();
    }

    /** Stops playing tone. */
    public void pause() {
        Log.i("DOPPLER","Stop playing");
        audioTrack.pause();
    }

    /** Generates tone to be played. */
    private void getTone() {
        //generate sample values
        double sample[] = new double[SAMPLES_NUM];
        for(int i=0; i<SAMPLES_NUM; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (SAMPLE_RATE / toneFrequency));
        }

        //convert to 16 bit pcm sound array
        int i=0;
        for(final double dVal : sample) {
            //scale to max amplitude (mul values from range <-1,1> by max short value)
            short val = (short) (dVal * Short.MAX_VALUE);

            //in 16 bit pc, first byte is low order byte
            generatedSound[i++] = (byte) (val & 0x00ff);
            //second is big order, but we have to move it to make it byte
            generatedSound[i++] = (byte) ((val & 0xff00) >>> 8);
        }
    }
}
