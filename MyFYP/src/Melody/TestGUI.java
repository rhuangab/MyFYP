package Melody;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TestGUI extends JFrame{
	private static String filename;
	private static MidiPlayer player;
	private static Midi myMidi;
	private static Sequencer sequencer;
	private static Sequence sequence;
	private static Sequence newSequence;
	private static ArrayList<JButton> tracks;
	private static ArrayList<JRadioButton> radios;
	private static ButtonGroup group;
	private static int numChannel=10;
	private static ArrayList<Integer> isOn;
	private boolean paused;
	private int mainChannel;
	private HashSet<Integer> onNote;
	private HashMap<Integer,Integer> velocityMap;
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		new TestGUI();
		
	}


	
	TestGUI() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		filename = "Without You";
		
		setUpPlayer();
		
		makeGUI();
		
		
		//initialize the isOn Array, 0 represents it is off
		isOn = new ArrayList();
		for(int i = 0; i< 16; i++) {
			isOn.add(new Integer(0));
		}
		
		close();
	
		//player.close();
		
		
	}
	//set up player 
	public void setUpPlayer() throws InvalidMidiDataException, IOException, MidiUnavailableException {
       // MidiPlayer player = new MidiPlayer();
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
		//sequencer = player.getSequencer();
	    //sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/"+filename+".mid"));
	    sequence = MidiSystem.getSequence(new File("midiLibrary/"+filename+".mid"));

	    //myMidi = new Midi(sequence,player.getSequencer().getTempoInBPM());
	    myMidi = new Midi(sequence,sequencer.getTempoInBPM());
		newSequence = myMidi.newSequence; 
		sequencer.setSequence(newSequence);
		//sequencer.start();
		//player.play(sequence, false);
		
	}
	
	
	
	
	//play only one specified channel
	public void solo(int num) {
		
		for(int i = 0; i < 16; ++i) {
			if(i!=num) 
				sequencer.setTrackMute(i, true);
			else
				sequencer.setTrackMute(i, false);
		}
		
	}
	
	public void makeGUI() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(filename + "  suggested: " + myMidi.getSuggestMainChannel());
        setSize(400,400);
        setLocationRelativeTo(null);
        tracks = new ArrayList();
        group = new ButtonGroup();
                GridLayout lay = new GridLayout(1,2);
        setLayout(lay);
        
        JPanel myPanel = new JPanel();
		GridLayout myLayout = new GridLayout(19,2);
		myPanel.setLayout(myLayout);
	
		
        JButton original = new JButton("Original");
        original.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					sequencer.open();
					sequencer.setSequence(sequence);
					
				} catch (MidiUnavailableException | InvalidMidiDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				reSet();
				if(isPaused() || sequencer.isRunning() == false){
					
					start();
				}
				else {
					stop();
					//setPaused(true);
				}
				
			}
        	
        });
        myPanel.add(original);
        JLabel original1 = new JLabel("     Duration:" + myMidi.getFinishTime(true));
       
        
        JButton separate = new JButton("New");
        separate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					sequencer.open();
					sequencer.setSequence(newSequence);
					
				} catch (MidiUnavailableException | InvalidMidiDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				reSet();
				if(isPaused() || sequencer.isRunning() == false){
					
					start();
				}
				else {
					stop();
					//setPaused(true);
				}
				
			}
        	
        });
        
        JLabel separate1 = new JLabel("     Duration:" + myMidi.getFinishTime(false));
        
        myPanel.add(original);
        myPanel.add(original1);
        myPanel.add(separate);
        myPanel.add(separate1);
        
        tracks.add(new JButton("1. Piano"));
        tracks.add(new JButton("2. Chromatic Percussion"));
        tracks.add(new JButton("3. Organ"));
        tracks.add(new JButton("4. Guitar"));
        tracks.add(new JButton("5. Bass"));
        tracks.add(new JButton("6. Strings"));
        tracks.add(new JButton("7. Ensemble"));
        tracks.add(new JButton("8. Brass"));
        tracks.add(new JButton("9. Reed"));
        tracks.add(new JButton("10. Pipe"));
        tracks.add(new JButton("11. Synth Lead"));
        tracks.add(new JButton("12. Synth Pad"));
        tracks.add(new JButton("13. Synth Effects"));
        tracks.add(new JButton("14. Ethnic"));
        tracks.add(new JButton("15. Percussvie"));
        tracks.add(new JButton("16. Sound Effects"));
       
        radios = new ArrayList();
        for(int i = 0 ;i < 16; ++i) {
        	JRadioButton temp = new JRadioButton(myMidi.getChannelInfo(i));
        	if(myMidi.newSequence.getTracks()[i].size() > 1000)
        		temp.setForeground(Color.BLUE);
             group.add(temp);
             radios.add(temp);
        		
        	
        }
       
        
        //set up the buttons
        for(int i = 0; i < 16; ++i) {
        	myPanel.add(tracks.get(i));
        	tracks.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						sequencer.open();
						sequencer.setSequence(newSequence);
						
					} catch (MidiUnavailableException | InvalidMidiDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					reSet();
					// TODO Auto-generated method stub
					Object source = e.getSource();
					if(source == tracks.get(0)) {
						sequencer.setTrackSolo(0, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(1)) {
						sequencer.setTrackSolo(1, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(2)) {
						sequencer.setTrackSolo(2, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(3)) {
						sequencer.setTrackSolo(3, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(4)) {
						sequencer.setTrackSolo(4, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(5)) {
						sequencer.setTrackSolo(5, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(6)) {
						sequencer.setTrackSolo(6, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(7)) {
						sequencer.setTrackSolo(7, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(8)) {
						sequencer.setTrackSolo(8, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(9)) {
						sequencer.setTrackSolo(9, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(10)) {
						sequencer.setTrackSolo(10, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(11)) {
						sequencer.setTrackSolo(11, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(12)) {
						sequencer.setTrackSolo(12, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(13)) {
						sequencer.setTrackSolo(13, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(14)) {
						sequencer.setTrackSolo(14, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else if(source == tracks.get(15)) {
						sequencer.setTrackSolo(15, true);
						if(isPaused() || sequencer.isRunning() == false){
							
							start();
						}
						else {
							stop();
							//setPaused(true);
						}
					}
					else {
						System.out.println("Error");
					}
				}
        		
        	});
        	
        	
        	myPanel.add(radios.get(i));
        	
        	
        }
       JButton submit = new JButton("SUBMIT");
       submit.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//find the chosen radioButton 
			try {
				generateFeatureFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
    	   
       });
       myPanel.add(submit);
        add(myPanel);
        
        
        //myPanel.setVisible(true);
        //myPanel.setVisible(true);
        setVisible(true);
        
	}
	
	public void reSet() {
		for(int i = 0; i < 16; ++i)
			sequencer.setTrackSolo(i, false);
	}

	
	 //Stops the sequencer and resets its position to 0.
	public void stop() {
		if (sequencer != null && sequencer.isOpen()) {
			System.out.println("Stop playing");
			sequencer.stop();
			sequencer.setMicrosecondPosition(0);
		}
	}

	 // start the sequencer.
		public void start() {
			if (sequencer != null && sequencer.isOpen()) {
				System.out.println("start playing");
				sequencer.start();
			}
		}

	 // Closes the sequencer.
	public void close() {
		if (sequencer != null && sequencer.isOpen()) {
			sequencer.close();
		}
	}
	
	 //Sets the paused state. Music may not immediately pause.
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

	//Returns the paused state.
	public boolean isPaused() {
		return paused;
	}
	
	//
	public int getSelectedButton() {
		int count = 0;
		  for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();
	            count ++;
	            if (button.isSelected())  {
	            	return count;
	            }
		  }
		  return count;
	}
	
	
	//generate feature file of the midi file
	public void  generateFeatureFile() throws IOException {
		onNote = new HashSet<Integer>();
		velocityMap = new HashMap<Integer,Integer>();
		mainChannel = getSelectedButton();
		
		FileWriter output = new FileWriter("MidiFeature/"+filename +".txt");
		FileWriter output2 = new FileWriter("MidiFeature2/"+filename +".txt");
		Track mainTrack = newSequence.getTracks()[mainChannel];
		for(int i = 0; i < mainTrack.size(); ++i) {
			MidiEvent event = mainTrack.get(i);
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                //if it is NOTE_ON command
                if(sm.getCommand() == 0x90) {
                	output.write(Double.toString(event.getTick()* myMidi.tickSize) + "   ");
                	if(sm.getData2() != 0) {
                    	output.write(sm.getData1() + "   "+ sm.getData2() +"   NOTE_ON" + "\n");
                    	onNote.add(new Integer(sm.getData1()));
                    	velocityMap.put(sm.getData1(), sm.getData2());
                    	
                	}
                	else {
                		output.write(sm.getData1() + "   "+ sm.getData2() +"   NOTE_ON" + "\n");
                		onNote.remove(new Integer(sm.getData1()));
                		velocityMap.remove(sm.getData1());
                	}
                	Object[] onnote =  onNote.toArray(); 
                	if(onnote.length > 0) {
                		output2.write(Double.toString(event.getTick()* myMidi.tickSize) + "   ");
                		for(int k = 0; k < onnote.length; ++k) {
                			output2.write(onnote[k] + ":" +  velocityMap.get(onnote[k]) + "   ");
                			}
                		output2.write("\n");  
                	}
                	  
            }
			
		}
		
		}
		if(output!=null)
			output.close();
		if(output2!=null)
			output2.close();
	}
	


}
