package rpg.Common;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

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
    sequencer.start();
  }

  private void stop() {
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
