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
    public static String filename;
    
    
    public static void main(String[] args) throws Exception {
    	
    	sequencer = MidiSystem.getSequencer();
    	
        //sequencer.open();
        
        filename = "Turkey";
        sequence = MidiSystem.getSequence(new File("midiLibrary/"+filename+".mid"));
        sequencer.setSequence(sequence);
       
        
        Midi myMidi = new Midi(sequence,sequencer.getTempoInBPM());
        myMidi.printTrackInformation(filename,true);
        myMidi.printTrackInformation(filename,false);
        //sequencer.stop();     


    }
	
}

