package Melody;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestGUI extends JFrame{
	private static String filename;
	private static MidiPlayer player;
	private static Midi myMidi;
	private static Sequencer sequencer;
	private static Sequence sequence;
	private static Sequence newSequence;
	private static ArrayList<JButton> tracks;
	private static ArrayList<JCheckBox> checkBox;
	private static int numChannel=10;
	private static ArrayList<Integer> isOn;
	private boolean paused;
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		new TestGUI();
		
	}


	
	TestGUI() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		filename = "Turkey";
		
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
	    sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/"+filename+".mid"));
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
        setTitle(filename);
        setSize(400,400);
        setLocationRelativeTo(null);
        tracks = new ArrayList();
        checkBox = new ArrayList();
                GridLayout lay = new GridLayout(1,2);
        setLayout(lay);
        
        JPanel myPanel = new JPanel();
		GridLayout myLayout = new GridLayout(18,2);
		myPanel.setLayout(myLayout);
	
		
        JButton original = new JButton("Original");
        myPanel.add(original);
        JCheckBox original1 = new JCheckBox("Duration:" + myMidi.getFinishTime(true));
        
        JButton separate = new JButton("New");
        JCheckBox separate1 = new JCheckBox("Duration:" + myMidi.getFinishTime(false));
        
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
       
        for(int i = 0 ;i < 16; ++i) {
        	JCheckBox temp = new JCheckBox(myMidi.getChannelInfo(i));
        	if(myMidi.newSequence.getTracks()[i].size() > 1000)
        		temp.setForeground(Color.BLUE);
             checkBox.add(temp);
        		
        	
        }
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
        	
        	myPanel.add(checkBox.get(i));
        	
        	
        }
       
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
	


}
