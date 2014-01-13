package Phonetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import FileLibraryPath.FileLibraryPath;

public class ExpandMultipleTimestamps {
	private Pattern timeStampPattern;
	private Pattern tagPattern;
	private final String timeStampPatternString = "((\\[\\d{1,2}:\\d{1,2}\\.\\d{1,2}\\])((\\[\\d{2}:\\d{2}\\.\\d{1,2}\\])*))(.*)";
	private final String tagPatternString = "(\\[ar:.*\\]|\\[ti:.*\\]|\\[al:.*\\]|\\[offset:.*\\])(\\s)*";
	private PriorityQueue<String> priorityQueue;
	private FileWriter lrcWriter;
	private File currentFile;
	public ExpandMultipleTimestamps(File inputFolder, File outputFolder) throws IOException
	{
		timeStampPattern = Pattern.compile(timeStampPatternString);
		tagPattern = Pattern.compile(tagPatternString);
		if(!outputFolder.exists())
			outputFolder.mkdir();
		File[] lrcList = inputFolder.listFiles();
		for(File lrcFile : lrcList)
		{
			currentFile = lrcFile;
			Scanner sc = new Scanner(new BufferedReader(new FileReader(lrcFile)));
			lrcWriter = new FileWriter(new File(outputFolder.getPath()+"/"+lrcFile.getName()));
			sortTimeStamp(sc);
		}
		/*File lrcFile = new File("/Users/apple/Downloads/song/lrc_midi_combine/lrc/ALL THE MAN THAT I NEED.lrc");
		Scanner sc = new Scanner(new BufferedReader(new FileReader(lrcFile)));
		
		lrcWriter = new FileWriter(new File(outputFolder.getPath()+"/"+lrcFile.getName()));
		sortTimeStamp(sc);*/
		
	}
	
	public void sortTimeStamp(Scanner sc) throws IOException
	{
		String last="";
		priorityQueue = new PriorityQueue<String>(500, new TimestampComparator());
		while (sc.hasNextLine()) {
			// System.out.println(s.nextLine());
			String line = sc.nextLine();
			//System.out.println(line+"1");
			if (!line.equals("")) {
				Matcher m1 = tagPattern.matcher(line);
				//System.out.println(line+"2");
				if(m1.matches())
				{
					lrcWriter.append(line+'\n');
					continue;
				}
				Matcher m2 = timeStampPattern.matcher(line);
				if(!m2.matches())
				{
					System.out.println(currentFile.getName()+"\n"+last+"\n"+line);
				}
				while(m2.matches())
				{
					//System.out.println(m.group(2)+m.group(5));
					priorityQueue.add(""+m2.group(2)+m2.group(5));
					m2 = timeStampPattern.matcher(m2.group(3)+m2.group(5));
				}
			}
			last = line;
		}
		writeToNewLrc();
	}
	
	public void writeToNewLrc() throws IOException
	{
		while(!priorityQueue.isEmpty())
		{
			//System.out.println(priorityQueue.poll());
			lrcWriter.append(priorityQueue.poll()+'\n');
			
		}
		lrcWriter.close();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File inputfolder = new File("/Users/apple/Documents/song/lrc_midi/lrcOrigin");
		File outputfolder = FileLibraryPath.LrcLibraryFolder;
		ExpandMultipleTimestamps expander = new ExpandMultipleTimestamps(inputfolder,outputfolder);
		/*Pattern p = Pattern.compile("\\[ar:.*\\]|\\[ti:.*\\]|\\[al:.*\\]");
		Matcher m = p.matcher("[ti:richeng]");
		if(m.matches())
			System.out.println(m.group(0));*/
	}

}
