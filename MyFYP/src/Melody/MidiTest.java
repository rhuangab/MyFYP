package Melody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * An example that plays a Midi sequence. First, the sequence is played once
 * with track 1 turned off. Then the sequence is played once with track 1 turned
 * on. Track 1 is the drum track in the example midi file.
 */
public class MidiTest implements MetaEventListener {

	// The drum track in the example Midi file
	private static final int MUTE_TRACK = 0;

	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		new MidiTest().run();
	}

	private MidiPlayer player;

	public void run() throws MidiUnavailableException, InvalidMidiDataException, IOException {

		player = new MidiPlayer();

		Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        
        Sequence sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/Without You.mid"));
        sequencer.setSequence(sequence);
        
        sequencer.stop(); 
        Midi myMidi = new Midi(sequencer, sequence);
        //myMidi.infoSequence(true);
        myMidi.SeperateChannel();
        Sequence newSequence = myMidi.newSequence;
       

		// play the sequence
        
        System.out.println("old sequence information: ");
        myMidi.printTrackInformation(sequence);
        System.out.println("new sequence information: ");
        //myMidi.printTrackInformation(newSequence);
        
        //player.play(sequence, true);
        
        //System.out.println("new seperate files");
		player.play(newSequence, true);

		// turn off the drums
		System.out.println("Playing without track " + MUTE_TRACK);
		sequencer = player.getSequencer();
		sequencer.setTrackMute(MUTE_TRACK, true);
		//player.play(newSequence,true);
		
		sequencer.addMetaEventListener(this);

	}

	/**
	 * This method is called by the sound system when a meta event occurs. In
	 * this case, when the end-of-track meta event is received, the mute track
	 * is turned on.
	 */
	public void meta(MetaMessage event) {
		if (event.getType() == MidiPlayer.END_OF_TRACK_MESSAGE) {
			Sequencer sequencer = player.getSequencer();
			if (sequencer.getTrackMute(MUTE_TRACK)) {
				// turn on the drum track
				System.out.println("Turning on drums...");
				sequencer.setTrackMute(MUTE_TRACK, false);
			} else {
				// close the sequencer and exit
				System.out.println("Exiting...");
				player.close();
				System.exit(0);
			}
		}
	}

}

class MidiPlayer implements MetaEventListener {

	// Midi meta event
	public static final int END_OF_TRACK_MESSAGE = 47;

	private Sequencer sequencer;

	private boolean loop;

	private boolean paused;

	/**
	 * Creates a new MidiPlayer object.
	 */
	public MidiPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);
		} catch (MidiUnavailableException ex) {
			sequencer = null;
		}
	}

	/**
	 * Loads a sequence from the file system. Returns null if an error occurs.
	 */
	public Sequence getSequence(String filename) {
		try {
			return getSequence(new FileInputStream(filename));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a sequence from an input stream. Returns null if an error occurs.
	 */
	public Sequence getSequence(InputStream is) {
		try {
			if (!is.markSupported()) {
				is = new BufferedInputStream(is);
			}
			Sequence s = MidiSystem.getSequence(is);
			is.close();
			return s;
		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Plays a sequence, optionally looping. This method returns immediately.
	 * The sequence is not played if it is invalid.
	 */
	public void play(Sequence sequence, boolean loop) {
		if (sequencer != null && sequence != null && sequencer.isOpen()) {
			try {
				sequencer.setSequence(sequence);
				sequencer.start();
				this.loop = loop;
			} catch (InvalidMidiDataException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method is called by the sound system when a meta event occurs. In
	 * this case, when the end-of-track meta event is received, the sequence is
	 * restarted if looping is on.
	 */
	public void meta(MetaMessage event) {
		if (event.getType() == END_OF_TRACK_MESSAGE) {
			if (sequencer != null && sequencer.isOpen() && loop) {
				sequencer.start();
			}
		}
	}

	/**
	 * Stops the sequencer and resets its position to 0.
	 */
	public void stop() {
		if (sequencer != null && sequencer.isOpen()) {
			sequencer.stop();
			sequencer.setMicrosecondPosition(0);
		}
	}

	/**
	 * Closes the sequencer.
	 */
	public void close() {
		if (sequencer != null && sequencer.isOpen()) {
			sequencer.close();
		}
	}

	/**
	 * Gets the sequencer.
	 */
	public Sequencer getSequencer() {
		return sequencer;
	}

	/**
	 * Sets the paused state. Music may not immediately pause.
	 */
	public void setPaused(boolean paused) {
		if (this.paused != paused && sequencer != null && sequencer.isOpen()) {
			this.paused = paused;
			if (paused) {
				sequencer.stop();
			} else {
				sequencer.start();
			}
		}
	}

	/**
	 * Returns the paused state.
	 */
	public boolean isPaused() {
		return paused;
	}

}
