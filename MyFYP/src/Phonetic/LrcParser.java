package Phonetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.Iterator;

import DatabaseManager.DatabaseManager;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

import FileLibraryPath.FileLibraryPath;

public class LrcParser {

	private Scanner sc;
	private FileWriter phone;
	private FileWriter cv;
	private FileWriter log;
	private String songName;
	private HTree hashtable;
	private Set<String> uniqueWordsInAllLrcs;
	private Set<String> uniqueWordsInOneLrc;
	private File m_dateFolder;

	public LrcParser(String sn, HTree ht, Set<String> uw, File dateFolder) throws IOException {
		songName = sn.replace(".lrc", "");
		hashtable = ht;
		phone = new FileWriter(dateFolder.getPath()
				+ "/phoneLibrary/" + songName + ".phone.txt");
		cv = new FileWriter(dateFolder.getPath() + "/cvLibrary/"
				+ songName + ".cv.txt");
		//log = new FileWriter("log.txt", true);
		sc = new Scanner(new BufferedReader(new FileReader(
				FileLibraryPath.LrcLibraryFolder.getPath() + "/" + songName
						+ ".lrc")));
		uniqueWordsInAllLrcs = uw;
		uniqueWordsInOneLrc = new TreeSet<String>();
		m_dateFolder = dateFolder;
	}

	public void performParse() throws IOException {
		String line;
		while (sc.hasNextLine()) {
			// System.out.println(s.nextLine());
			line = sc.nextLine();
			if (!line.equals("")) {
				// find all the contents in the brackets [], also consider cases
				// where there are multiple timestamps
				int position = line.lastIndexOf("]");

				String timestamp = null;

				if (position != -1) {
					if (position + 1 <= line.length() - 1) {
						timestamp = line.substring(0, position + 1);
						line = line.substring(position + 1);
					} else {
						timestamp = line;
						line = null;
					}
				}

				List<String> strArr = new ArrayList();
				if (line != null) {
					// expand all the numbers in the sentence
					line = expandNum(line);
					line = preprocessOne(line);	
					String[] strArr1 = line.split(" ");
					for (int i = 0; i < strArr1.length; ++i) {
						// trim the blank spaces at the ends of the words
						strArr1[i] = strArr1[i].trim();
						if (!strArr1[i].equals(""))
							strArr.add(strArr1[i]);
					}
					phone.write(timestamp);
					cv.write(timestamp);

					for (int i = 0; i < strArr.size(); ++i) {
						WordStruct myWord = (WordStruct) hashtable.get(strArr
								.get(i));
						if (myWord != null) {
							phone.write(myWord.stress + " ");
							cv.write(myWord.cv + " ");
							// System.out.println(myWord.word + "  " +
							// myWord.stress + "  " + myWord.cv);
						} else {
							uniqueWordsInAllLrcs.add(strArr.get(i));
							uniqueWordsInOneLrc.add(strArr.get(i));
							phone.write("UNDEF" + " ");
							cv.write("UNDEF" + "  ");
							// System.out.println(strArr.get(i) +
							// " is not in the dictionary");
						}
					}
					phone.write("\n");
					cv.write("\n");
				}

				/*
				 * System.out.print(timestamp); for (int i = 0; i <
				 * strArr.size(); ++i) { System.out.print("[" + strArr.get(i) +
				 * "]"); } System.out.println();
				 */
			}
		}
		writeToLogFromOneLrc();

		if (sc != null)
			sc.close();
		if (phone != null)
			phone.close();
		if (cv != null)
			cv.close();
		if (log != null)
			log.close();

	}
	
	public void writeToLogFromOneLrc() throws IOException
	{
		File log_withLrcName = new File(m_dateFolder.getPath()+"/log/log_withLrcName.txt");
		if(!log_withLrcName.getParentFile().exists())
			log_withLrcName.getParentFile().mkdir();
		FileWriter log = new FileWriter(log_withLrcName, true);
		if (!uniqueWordsInOneLrc.isEmpty()) {
			log.write("["+songName+".lrc]\n");
			java.util.Iterator<String> iter = uniqueWordsInOneLrc.iterator();
			//log.write("[ti:" + songName + ".lrc]\n");
			while (iter.hasNext()) {
				log.write(iter.next() + "\n");
			}
		}
		log.close();
	}
	
	public static String getCurrentDate()
	{
		DateFormat df = new SimpleDateFormat("MMM.dd @ HH.mm");
		return df.format(new Date());
	}

	public static void writeToLogTogether(Set<String> uniqueWords,String cd) throws IOException {
		File log_noLrc = new File(FileLibraryPath.DataOutputFolder.getPath()+"/"+cd+"/log/log_noLrcName.txt");
		if(!log_noLrc.getParentFile().exists())
			log_noLrc.getParentFile().mkdir();
		FileWriter log = new FileWriter(log_noLrc, true);
		if (!uniqueWords.isEmpty()) {
			java.util.Iterator<String> iter = uniqueWords.iterator();
			//log.write("[ti:" + songName + ".lrc]\n");
			while (iter.hasNext()) {
				log.write(iter.next() + "\n");
			}
		}
		log.close();
	}

	// judge if a a character is a punctuation
	public String removePunc(String s) {
		s = s.replace('!', ' ');
		s = s.replace('?', ' ');
		s = s.replace(',', ' ');
		s = s.replace('.', ' ');
		s = s.replace('(', ' ');
		s = s.replace(')', ' ');
		s = s.replace("-", "");
		return s;
	}

	public String expandNum(String s) {
		s = s.replace("0", " ZERO ");
		s = s.replace("1", " ONE ");
		s = s.replace("2", " TWO ");
		s = s.replace("3", " THREE ");
		s = s.replace("4", " FOUR ");
		s = s.replace("5", " FIVE ");
		s = s.replace("6", " SIX ");
		s = s.replace("7", " SEVEN ");
		s = s.replace("8", " EIGHT ");
		s = s.replace("9", " NINE ");
		return s;

	}

	// remove some abbreviation(including possessive, abbreviation for am,
	// is,have and so on)
	public String removeAbb(String s) {
		/*
		 * s = s.replace("'s", ""); s = s.replace("'ve", ""); s =
		 * s.replace("'m", ""); s = s.replace("'ll", "");
		 */
		return s;
	}

	// preprocess one: capitalize every letter, remove possessive and
	// abbreviation('s, 'm, 've, '), and all the punctuation(one time one
	// sentence)
	public String preprocessOne(String line) {
		// ArrayList<String> newList = new ArrayList<String>();
		/*String word;
		for (int i = 0; i < strArr.size(); ++i) {
			word = strArr.get(i);
			word = removePunc(word);
			// word = removeAbb(word);
			word = word.toUpperCase();
			strArr.set(i, word.trim());
		}*/
		line = removePunc(line);
		line = line.toUpperCase();
		return line;
	}

	public static void parseAllLrcFromAFolder(File folder) throws IOException {
		File[] lrcList = folder.listFiles();
		String cd = getCurrentDate();
		File phoneLibraryFolder = new File(FileLibraryPath.DataOutputFolder.getPath()+"/"+cd+"/phoneLibrary/");
		File cvLibraryFolder = new File(FileLibraryPath.DataOutputFolder.getPath()+"/"+cd+"/cvLibrary/");
		phoneLibraryFolder.mkdirs();
		cvLibraryFolder.mkdir();
		File dateFolder = phoneLibraryFolder.getParentFile();
		
		HTree hashtable = DatabaseManager.getHashtableSingleton();
		Set<String> uniqueWords = new TreeSet<String>();
		for (File lrcFile : lrcList) {
			LrcParser lrcParser = new LrcParser(lrcFile.getName(), hashtable,uniqueWords,dateFolder);
			lrcParser.performParse();
		}
		writeToLogTogether(uniqueWords,cd);
		// recman.commit();
		DatabaseManager.close();

	}

	public static void main(String[] args) throws IOException {
		// LrcParser lrcParser = new LrcParser("A MOMENT LIKE THIS");
		// lrcParser.sortTimeStamp();
		/**
		 * Before running, please change the encoding type to UTF-8. Encoding
		 * type could be set in Run Configuration->Common.
		 */
		parseAllLrcFromAFolder(FileLibraryPath.LrcLibraryFolder);
	}

}