package rpg.engine.music;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum MusicSystem {
    INSTANCE;

    private Clip clip;

    MusicSystem() {
        // Nothing to initialize at startup
    }

    public void playFile(InputStream wavFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        stop();

        BufferedInputStream bufferedWavFile = new BufferedInputStream(wavFile);
        AudioInputStream sourceStream = AudioSystem.getAudioInputStream(bufferedWavFile);

        // Target format: 16-bit signed PCM, little endian
        AudioFormat baseFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2, // 16-bit = 2 bytes per sample
                baseFormat.getSampleRate(),
                false // little endian
        );

        InputStream bufferedIn = new BufferedInputStream(sourceStream);
        AudioInputStream convertedStream = new AudioInputStream(bufferedIn, targetFormat, sourceStream.getFrameLength());

        clip = AudioSystem.getClip();
        clip.open(convertedStream);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null) {
            stop();
            clip.close();
            clip = null;
        }
    }
}
