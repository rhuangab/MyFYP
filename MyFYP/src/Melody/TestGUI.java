package Melody;

import java.awt.GridLayout;
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

public class TestGUI {
	private static String filename;
	private static MidiPlayer player;
	private static Sequencer sequencer;
	private static Sequence sequence;
	private static Sequence newSequence;
	private static ArrayList<JButton> tracks;
	private static ArrayList<JCheckBox> checkBox;
	private static int numChannel=10;
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		filename = "Turkey";
		//setUpPlayer();
		
		//player.close();
		sequencer = MidiSystem.getSequencer();
        sequencer.open();
		sequence = MidiSystem.getSequence(new File("/Users/jenny/git/MyFYP/MyFYP/midiLibrary/"+filename+".mid"));
		//Midi myMidi = new Midi(sequencer, sequence);
		//newSequence = myMidi.newSequence;
		//numChannel = newSequence.getTracks().length;
		//System.out.println(numChannel);
		//sequencer.setSequence(sequence);
		sequencer.close();
		//sequencer.start();
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
	
	public TestGUI() {
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle(filename);
        guiFrame.setSize(400,400);
        guiFrame.setLocationRelativeTo(null);
        tracks = new ArrayList();
        checkBox = new ArrayList();
        
        GridLayout lay = new GridLayout(1,2);
        guiFrame.setLayout(lay);
        
        JPanel leftPanel = new JPanel();
		GridLayout myLayout = new GridLayout(18,1);
		leftPanel.setLayout(myLayout);
	
		JPanel rightPanel = new JPanel();
		leftPanel.setLayout(myLayout);
		
        JButton original = new JButton("Original");
        guiFrame.add(original);
        JCheckBox original1 = new JCheckBox();
        original1.setVisible(false);
        JButton separate = new JButton("Separate");
        JCheckBox separate1 = new JCheckBox();
        separate1.setVisible(false);
        leftPanel.add(original);
        rightPanel.add(original1);
        leftPanel.add(separate);
        rightPanel.add(separate1);
        
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
       
        
        for(int i = 0; i < tracks.size(); ++i) {
        	leftPanel.add(tracks.get(i));
        }
       
        for(int i = 0 ;i <16; ++i) {
        	
        }
        guiFrame.add(leftPanel);
        guiFrame.add(rightPanel);
        
        //leftPanel.setVisible(true);
        //rightPanel.setVisible(true);
        guiFrame.setVisible(true);
	}

}
