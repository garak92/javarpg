package rpg.engine.music;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;

public enum MusicSystem {
    INSTANCE;

    private Sequencer sequencer = null;

    MusicSystem() {
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer == null) {
                throw new MidiUnavailableException();
            }
            sequencer.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playFile(InputStream midiFile) throws IOException, InvalidMidiDataException {
        stop();
        Sequence sequence = MidiSystem.getSequence(midiFile);
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.start();
    }

    public void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
    }

    public void close() {
        if (sequencer != null && sequencer.isOpen()) {
            stop();
            sequencer.close();
        }
    }

}
