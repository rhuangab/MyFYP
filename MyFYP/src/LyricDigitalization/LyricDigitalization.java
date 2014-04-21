package LyricDigitalization;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import jdbm.htree.HTree;

import DatabaseManager.DatabaseManager;
import FileLibraryPath.FileLibraryPath;
import MucisXML.MyNote;
import MucisXML.MyPart;
import MucisXML.MySingleWord;
import MucisXML.XMLParser;
import Phonetic.WordStruct;

public class LyricDigitalization {
	private List<MyPart> myParts;
	private HTree hashtable;
	private String songName;

	public LyricDigitalization() throws Exception {
		
		for (File subFolder : FileLibraryPath.NWCFolder.listFiles()) {
			if(subFolder.getName().equals(".DS_Store")) continue;
			System.out.println(subFolder.getName());
			File[] xmlFiles = subFolder
					.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith("xml");
						}
					});
			for (File xml : xmlFiles) {
				songName = xml.getName();
				myParts = new XMLParser(xml).getAllParts();
				hashtable = DatabaseManager.getHashtableSingleton();
				outputPitchAndSyllablesToFile(xml.getName().replace(".xml", ""));
			}
		}
		/*myParts = new XMLParser(new File("/Users/apple/Dropbox/midi file/nwc/wang & bingo's/anastacia-everything_burns_feat_ben_moody.xml")).getAllParts();
		hashtable = DatabaseManager.getHashtableSingleton();
		outputPitchAndSyllablesToFile("anastacia-everything_burns_feat_ben_moody");*/
	}

	public void outputWordAndStress() throws IOException {
		MyPart part = myParts.get(0);
		MySingleWord ws = null;
		while ((ws = part.getNextWord()) != null) {
			System.out.println(ws.getWordText() + ": "
					+ getStress(ws.getWordText().toUpperCase()).length() + ":"
					+ ws.numOfSyllables());
		}
	}
	

	public void outputPitchAndSyllablesToFile(String filename) throws IOException {
		String outputFolderPath = FileLibraryPath.ParserOutputFolder.getPath();
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(outputFolderPath+"/"+filename+".txt")));
		MyPart part = myParts.get(0);
		MySingleWord singleWord = null;
		while ((singleWord = part.getNextWord()) != null) {
			String wordText = singleWord.getWordText();
			analyse(singleWord, fileWriter);
		}
		fileWriter.close();
	}
	
	public void analyse(MySingleWord singleWord, PrintWriter fileWriter) throws IOException
	{
		boolean hasAlreadyOutput = false;
		String wordText = singleWord.getWordText();
		String stress = getStress(wordText);
		if(stress == null && wordText.contains("'"))
		{
			stress = getStress(wordText.replaceAll("'", ""));
			if(stress != null ) {
				singleWord.setWordText(wordText.replaceAll("'", ""));
				wordText = singleWord.getWordText();
			}
		}
		if(stress == null && wordText.contains(" "))
		{
			stress = getStress(wordText.replaceAll(" ", ""));
			if(stress != null ) {
				singleWord.setWordText(wordText.replaceAll(" ", ""));
				wordText = singleWord.getWordText();
			}
		}
		if(stress == null && wordText.contains(" "))
		{
			String[] brokenParts = wordText.split(" ");
			boolean areWords = true;
			String outputString = "";
			for(String snippet : brokenParts)
			{
				String smallStress = getStress(snippet);
				if(smallStress == null)
				{
					areWords = false;
					outputString = "";
					break;
				}
			}
			if(areWords)
			{
				hasAlreadyOutput = true;
				for(String snippet : brokenParts)
				{
					String smallStress = getStress(snippet);
					MySingleWord newSingleWord = new MySingleWord(singleWord.getNotes().get(0));
					newSingleWord.setWordText(snippet);
					outputString = getOutputString(newSingleWord, smallStress);
					fileWriter.print(outputString);
				}
			}
		}
		if (stress == null && !hasAlreadyOutput) {
			boolean areWords = true;
			String outputString = "";
			Iterator<MyNote> itr = singleWord.getNotes().iterator();
			while (itr.hasNext()) {
				MyNote curNote = itr.next();
				String smallStress = getStress(curNote.getLyricContent());
				if (smallStress == null) {
					areWords = false;
					outputString = "";
					break;
				}
			}
			if(areWords)
			{
				hasAlreadyOutput = true;
				int numOfSyllables = singleWord.numOfSyllables();
				//outputString = String.format("%-15s [%d Syllabels]  ", "\""+wordText+"\"",numOfSyllables) + ":{";
				outputString = "";
				itr = singleWord.getNotes().iterator();
				int weight = 0;
				while (itr.hasNext()) {
					MyNote curNote = itr.next();
					String smallStress = getStress(curNote.getLyricContent());
					outputString += curNote.getLyricContent() + ","
							+ smallStress.substring(0, 1) + ","
							+ curNote.getPitchValue() + ","
							+ curNote.getRythm() + ','
							+ curNote.getDuration() + '\n';
				}
				fileWriter.print(outputString);
			}
		}
		int numOfSyllables = singleWord.numOfSyllables();
		/*if (stress == null && !wordText.equals("") && !hasAlreadyOutput) {
			fileWriter.println("This Following Word Not Found *********");
			stress = "0";
		}*/
		if(stress == null) stress = "0";
		while (numOfSyllables > stress.length()) {
			stress += "0";
		}
		wordText = wordText.trim();
		if(!hasAlreadyOutput)
		{
			String outputString = "";
			outputString = getOutputString(singleWord, stress);
			fileWriter.print(outputString);
		}
	}

	public String getOutputString(MySingleWord singleWord, String stress)
	{
		String wordText = singleWord.getWordText(); 
		int numOfSyllables = singleWord.numOfSyllables();
		//String outputString = String.format("%-15s [%d Syllabels]  ", "\""+wordText+"\"",numOfSyllables) + ":{";
		int weight = 0;
		String outputString = "";
		for (int i = 0; i < numOfSyllables; ++i) {
			MyNote currentNote = singleWord.getNextNote();
			outputString += currentNote.getLyricContent() + ","
					+  stress.substring(i, i + 1) + ","
					+ currentNote.getPitchValue() + ","
					+ currentNote.getRythm() + ','
					+ currentNote.getDuration() + '\n';
			
		}
		return outputString;
	}
	
	
	public String getStress(String word) throws IOException {
		WordStruct ws = (WordStruct) hashtable.get(word.toUpperCase());
		return ws == null ? null : ws.stress;
	}

	public String getNextWordString(MyPart part) {
		MySingleWord ms = part.getNextWord();
		return ms == null ? null : ms.getWordText();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		LyricDigitalization ld = new LyricDigitalization();
		long end = System.currentTimeMillis();
		System.out.println((end-begin)/1000);
	}
}
