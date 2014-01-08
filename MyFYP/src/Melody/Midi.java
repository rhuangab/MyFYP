package Melody;

import java.io.FileWriter;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Midi {
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int CONTROL_CHANGE = 0xB0;
    public static final int NUM_OF_TRACK = 30;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public double tickSize;
    public double tempoInBPM;
    //public Sequencer sequencer;
    public Sequence sequence;
    public Sequence newSequence;
    private float divisionType;
    
   //calculate timestamp for each MidiEvent
    public double calTickSize() {
    	double tickSize;
    	double ticksPerSecond;
        if(divisionType == Sequence.PPQ) {
        	ticksPerSecond =  sequence.getResolution() * (tempoInBPM / 60.0);
        	tickSize = 1.0 / ticksPerSecond;
        	//System.out.println("Outter PPQ type: sequencer tempoInBMP: " + sequencer.getTempoInBPM() + " resolution: " +sequence.getResolution() + "tickSize: " + tickSize );

        }
        else {
        	//System.out.println("SMPTE type");
        	 double framesPerSecond = 
        		  (divisionType == Sequence.SMPTE_24 ? 24
        		    : (divisionType == Sequence.SMPTE_25 ? 25
        		      : (divisionType == Sequence.SMPTE_30 ? 30
        		        : (divisionType == Sequence.SMPTE_30DROP ? 29.97:29.97))));
        		 ticksPerSecond = sequence.getResolution() * framesPerSecond;
        		 tickSize = 1.0 / ticksPerSecond;
        }
        return tickSize;
    	
    }
    
    //collect track information
    public void printTrackInformation(String filename, boolean old) throws IOException {
    	Sequence sequence;
    	FileWriter output = null;
    	if(old) {
    		sequence = this.sequence;
    		output = new FileWriter("/Users/jenny/git/MyFYP/MyFYP/MidiInfoPack/old"+filename+".txt");
    		
    	}
    	else {
    		sequence = this.newSequence;
    		output = new FileWriter("/Users/jenny/git/MyFYP/MyFYP/MidiInfoPack/new"+filename+".txt");
    	}
    	
    	output.write("There are total " + sequence.getTracks().length + " track in this midi file"+"\n");
    	//output.write("The division type for this sequence is equal to the old one " + (sequence.getDivisionType() == Sequence.PPQ) + " resolution is " + sequence.getResolution() +"\n");
 
    	int num = 0;
    	for(Track track : sequence.getTracks()) {
    		num++;
    		output.write("Track " + num + ":size = " + track.size() + "\n");
    	}
    	int trackNumber = 0;
    	int numOfBytes = 0;
    	for(Track track : sequence.getTracks()) {
    		trackNumber++;
    		numOfBytes += track.size();
    		output.write("Track " + trackNumber + ": size = " + track.size()+"\n" );
    		
    		for(int i = 0; i < track.size();++i) {
    			 MidiEvent event = track.get(i);
                 //tickSize = calTickSize();
                 output.write("@" + event.getTick() + " $" + event.getTick()* tickSize +" ");
                 MidiMessage message = event.getMessage();
                 if (message instanceof ShortMessage) {
                	 
                     ShortMessage sm = (ShortMessage) message;
                     output.write("Channel: " + sm.getChannel() + " ");
                     if (sm.getCommand() == NOTE_ON) {
                         int key = sm.getData1();
                         int octave = (key / 12)-1;
                         int note = key % 12;
                         String noteName = NOTE_NAMES[note];
                         int velocity = sm.getData2();
                         if(velocity == 0) {
                         	output.write("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity+"\n");
                         }else {
                         	output.write("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity+"\n");
                         }
              
                     } else if (sm.getCommand() == NOTE_OFF) {
                         int key = sm.getData1();
                         int octave = (key / 12)-1;
                         int note = key % 12;
                         String noteName = NOTE_NAMES[note];
                         int velocity = sm.getData2();
                         output.write("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity+"\n");
                         
                     } else if(sm.getCommand() == CONTROL_CHANGE) {
                     	output.write("Control Change message"+"\n");
                     	
                     } else {
                        output.write("Other short message:" + sm.getCommand()+"\n");
                     }
                 
                     
                 } else {
                     output.write("Other meta message: " + message.getClass()+"\n");
                 }
    		}
    		output.write("\n");
    	}
    	output.write("The total size of the sequence is " + numOfBytes);
    }
    
   
    
    //convert old sequence to new sequence where each track has one channel for human test
    public void seperateChannel() throws InvalidMidiDataException {
    	newSequence = new Sequence(divisionType,sequence.getResolution());
    	for(int i = 0; i < 16 ;++i) {
    		newSequence.createTrack();
    	}
    	Track[] newTrack = newSequence.getTracks();
    	
    	int trackNumber = 0;
    	//System.out.println("orignal length is" + sequence.getTracks().length);
    	for (Track track :  sequence.getTracks()) {
            trackNumber++;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                //tickSize = calTickSize();
                //System.out.println("@" + event.getTick() * tickSize + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    
                    int numChannel = sm.getChannel();
                    newTrack[numChannel].add(event);
                    //System.out.println(" new tick size is " + )
                    //System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        if(velocity == 0) {
                        	//System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        }else {
                        	//System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        }
             
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        
                    } else if(sm.getCommand() == CONTROL_CHANGE) {
                    	//System.out.println("this is message control change");
                    	
                    } else {
                       // System.out.println("Command:" + sm.getCommand());
                    }
                    
                } else {
                	//newTrack[NUM_OF_TRACK-1].add(event);
                    //System.out.println("Other message: " + message.getClass());
                }
            }
        }
    	//System.out.println("original length is" + sequence.getTracks().length);
    	//System.out.println("new  length is" + newSequence.getTracks().length);
    	//System.out.println("judge: " + (newSequence == this.newSequence));
   }
  
    //get information of a channel of the new Sequence 
    public String getChannelInfo(int num) {
    	String info = "NULL";
    	String size;
    	String startTime;
    	String endTime;
    	Track current = newSequence.getTracks()[num];
    	size = Double.toString(current.size());
    	if(current == null || current.size() == 0)
    		return info;

    	startTime = Double.toString(current.get(0).getTick() * tickSize);
    	endTime = Double.toString(current.get(current.size()-1).getTick() * tickSize);
    	info =size + "   " + startTime + "   " + endTime;
    	return info;
    }
    
    Midi(Sequence sequence,double tempo) throws InvalidMidiDataException {
    	divisionType = sequence.getDivisionType();
    	this.sequence = sequence;
    	//this.sequencer = sequencer;
    	tickSize = calTickSize();
    	this.tempoInBPM = tempo;
        seperateChannel();
    	}

}
