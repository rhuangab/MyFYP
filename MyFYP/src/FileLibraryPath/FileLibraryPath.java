package FileLibraryPath;

import java.io.File;

public class FileLibraryPath {
	
	static{
		CvLibraryFolder = new File("/Users/apple/Documents/song/cvLibrary");
		PhoneLibraryFolder = new File("/Users/apple/Documents/song/phoneLibrary");
		LrcLibraryFolder = new File("/Users/apple/Documents/song/lrc_midi/lrcExpanded_useThis");
		MidiLibraryFolder = new File("Somewhere");
	}
	public static final File LrcLibraryFolder;
	public static final File MidiLibraryFolder;
	public static final File CvLibraryFolder;
	public static final File PhoneLibraryFolder;
}
