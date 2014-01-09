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
	
	TestGUI() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		filename = "Turkey";
		//setUpPlayer();
		
		//player.close();
		sequencer = MidiSystem.getSequencer();
        //sequencer.open();
	    sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/"+filename+".mid"));
		sequencer.setSequence(sequence);
		
		myMidi = new Midi(sequence,sequencer.getTempoInBPM());
		newSequence = myMidi.newSequence;
		numChannel = newSequence.getTracks().length;
		makeGUI();
		
		//initialize the isOn Array, 0 represents it is off
		isOn = new ArrayList();
		for(int i = 0; i< 16; i++) {
			isOn.add(new Integer(0));
		}
		
		//sequencer.close();
		//sequencer.start();
		
		
	}
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		new TestGUI();
	}
	
	/*public static void setUpPlayer() throws MidiUnavailableException, InvalidMidiDataException, IOException {

		player = new MidiPlayer();
		sequencer = MidiSystem.getSequencer();
        sequencer.open();
		sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/"+filename+".mid"));
		Midi myMidi = new Midi(sequencer, sequence);
		newSequence = myMidi.newSequence;
		
	    
        sequencer.setSequence(sequence);
        //sequencer.stop(); 
	}*/
	
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
					// TODO Auto-generated method stub
					Object source = e.getSource();
					if(source == tracks.get(0)) {
						if(isOn.get(0).equals(0)) {
							sequencer.close();
							isOn.set(0, new Integer(1));
						}
							
			
						else {
							sequencer.setTrackSolo(0, true);
							isOn.set(0, new Integer(0));
							sequencer.start();
						}
					}
					else if(source == tracks.get(1)) {
						if(isOn.get(1).equals(1)) {System.out.println("get into here1");
						if(sequencer.isOpen())
							sequencer.close();
							isOn.set(0, new Integer(1));
						}
							
			
						else {System.out.println("get into here2");
						    try {
								sequencer.open();
							} catch (MidiUnavailableException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
							//sequencer.setTrackSolo(1, true);
							isOn.set(1, new Integer(0));
							sequencer.start();
						}
					}
					sequencer.close();
					
						
					
				}
        		
        	});
        	myPanel.add(checkBox.get(i));
        	
        	
        }
       
        add(myPanel);
        
        
        //myPanel.setVisible(true);
        //myPanel.setVisible(true);
        setVisible(true);
        
	}
	


}
