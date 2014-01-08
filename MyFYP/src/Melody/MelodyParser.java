package Melody;

import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class MelodyParser {
	
    public static Sequencer sequencer;
    public static Sequence sequence;
    
    
    public static void main(String[] args) throws Exception {
    	
    	sequencer = MidiSystem.getSequencer();
        sequencer.open();
        
        sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/Without You.mid"));
        sequencer.setSequence(sequence);
        
        //Synthesizer synthesizer = MidiSystem.getSynthesizer();
    	//synthesizer.open();//
        
        sequencer.stop(); 
        Midi myMidi = new Midi(sequencer, sequence);
        //myMidi.SeperateChannel();
        Sequence newSequence = myMidi.newSequence;
        myMidi.printTrackInformation(sequence);
        //myMidi.printTrackInformation(myMidi.newSequence);
                

        //synthesizer.close();

    }
	
}

