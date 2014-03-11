package FileLibraryPath;

import java.io.File;
import java.util.HashMap;

public class FileLibraryPath {
	public static final HashMap<String, Integer> beatsMap;
	public static final HashMap<String, Integer> stepMap;
	static{
		CvLibraryFolder = new File("/Users/apple/Documents/song/cvLibrary");
		PhoneLibraryFolder = new File("/Users/apple/Documents/song/phoneLibrary");
		LrcLibraryFolder = new File("/Users/apple/Documents/song/lrc_midi/lrcExpanded_useThis_0115");
		MidiLibraryFolder = new File("Somewhere");
		//Google Drive
		//DataOutputFolder = new File("/Users/apple/Google Drive/HKUST Courses/FYP/Share/data");
		//Dropbox
		DataOutputFolder = new File("/Users/apple/Dropbox/FYP/data");
		ParserOutputFolder = new File("/Users/apple/Dropbox/midi file/output");
		NWCFolder = new File("/Users/apple/Dropbox/midi file/nwc/");
		//NWCFolder = new File("/Users/apple/Dropbox/midi file (2)/bucket1");
		//NWCFolder = new File("/Users/apple/Dropbox/midi file (2)/--__--/wang & bingo's");
		//NWCFolder = new File("/Users/apple/Dropbox/midi file (2)/wangbo");
		
		stepMap = new HashMap<String,Integer>();
		stepMap.put("whole", 64);
		stepMap.put("half", 32);
		stepMap.put("quarter", 16);
		stepMap.put("eighth", 8);
		stepMap.put("16th", 4);
		stepMap.put("32nd", 2);
		stepMap.put("64th", 1);
		
		beatsMap = new HashMap<String,Integer>();
		beatsMap.put("2+2+1", 3);
		beatsMap.put("2+2+2", 3);
		
		beatsMap.put("4+1+1", 3);
		
		beatsMap.put("4+2+1", 3);
		beatsMap.put("4+2+2", 1);
		
		beatsMap.put("4+3+1", 3);
		beatsMap.put("4+3+2", 1);
		beatsMap.put("4+3+3", 1);
		
		beatsMap.put("4+4+1", 3);
		beatsMap.put("4+4+2", 1);
		beatsMap.put("4+4+3", 2);
		beatsMap.put("4+4+4", 1);
		
		beatsMap.put("4+5+1", 3);
		beatsMap.put("4+5+2", 1);
		beatsMap.put("4+5+3", 3);
		beatsMap.put("4+5+4", 1);
		beatsMap.put("4+5+5", 1);
		
		beatsMap.put("4+6+1", 3);
		beatsMap.put("4+6+2", 1);
		beatsMap.put("4+6+3", 1);
		beatsMap.put("4+6+4", 2);
		beatsMap.put("4+6+5", 1);
		beatsMap.put("4+6+6", 1);
		
		beatsMap.put("8+3+1", 3);
		beatsMap.put("8+3+2", 1);
		beatsMap.put("8+3+3", 1);
		
		beatsMap.put("8+4+1", 3);
		beatsMap.put("8+4+2", 1);
		beatsMap.put("8+4+3", 2);
		beatsMap.put("8+4+4", 1);
		
		beatsMap.put("8+6+1", 3);
		beatsMap.put("8+6+2", 1);
		beatsMap.put("8+6+3", 1);
		beatsMap.put("8+6+4", 2);
		beatsMap.put("8+6+5", 1);
		beatsMap.put("8+6+6", 1);
		
		beatsMap.put("8+12+1", 3);
		beatsMap.put("8+12+2", 1);
		beatsMap.put("8+12+3", 1);
		beatsMap.put("8+12+4", 2);
		beatsMap.put("8+12+5", 1);
		beatsMap.put("8+12+6", 1);
		beatsMap.put("8+12+7", 2);
		beatsMap.put("8+12+8", 1);
		beatsMap.put("8+12+9", 1);
		beatsMap.put("8+12+10", 2);
		beatsMap.put("8+12+11", 1);
		beatsMap.put("8+12+12", 1);
	}
	public static final File LrcLibraryFolder;
	public static final File MidiLibraryFolder;
	public static final File CvLibraryFolder;
	public static final File PhoneLibraryFolder;
	public static final File DataOutputFolder;
	public static final File NWCFolder;
	public static final File ParserOutputFolder;
	
}
