package Phonetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	private Set<String> uniqueWords;

	public LrcParser(String sn, HTree ht) throws IOException {
		songName = sn.replace(".lrc", "");
		hashtable = ht;
		phone = new FileWriter(FileLibraryPath.PhoneLibraryFolder.getPath()
				+ "/" + songName + ".phone.txt");
		cv = new FileWriter(FileLibraryPath.CvLibraryFolder.getPath() + "/"
				+ songName + ".cv.txt");
		log = new FileWriter("log.txt", true);
		sc = new Scanner(new BufferedReader(new FileReader(
				FileLibraryPath.LrcLibraryFolder.getPath() + "/" + songName
						+ ".lrc")));
		uniqueWords = new TreeSet<String>();
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
					String[] strArr1 = line.split(" ");
					for (int i = 0; i < strArr1.length; ++i) {
						// trim the blank spaces at the ends of the words
						strArr1[i] = strArr1[i].trim();
						if (!strArr1[i].equals(""))
							strArr.add(strArr1[i]);
					}
					phone.write(timestamp);
					cv.write(timestamp);

					preprocessOne(strArr);
					for (int i = 0; i < strArr.size(); ++i) {
						WordStruct myWord = (WordStruct) hashtable.get(strArr
								.get(i));
						if (myWord != null) {
							phone.write(myWord.stress + " ");
							cv.write(myWord.cv + " ");
							// System.out.println(myWord.word + "  " +
							// myWord.stress + "  " + myWord.cv);
						} else {
							uniqueWords.add(strArr.get(i));
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
		writeToLog();

		if (sc != null)
			sc.close();
		if (phone != null)
			phone.close();
		if (cv != null)
			cv.close();
		if (log != null)
			log.close();

	}

	public void writeToLog() throws IOException {
		if (!uniqueWords.isEmpty()) {
			java.util.Iterator<String> iter = uniqueWords.iterator();
			log.write("[ti:" + songName + ".lrc]\n");
			while (iter.hasNext()) {
				log.write(iter.next() + "\n");
			}
		}
	}

	// judge if a a character is a punctuation
	public String removePunc(String s) {
		s = s.replace('!', '\0');
		s = s.replace('?', '\0');
		s = s.replace(',', '\0');
		s = s.replace('.', '\0');
		s = s.replace('(', '\0');
		s = s.replace(')', '\0');

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
	public void preprocessOne(List<String> strArr) {
		// ArrayList<String> newList = new ArrayList<String>();
		String word;
		for (int i = 0; i < strArr.size(); ++i) {
			word = strArr.get(i);
			word = removePunc(word);
			// word = removeAbb(word);
			word = word.toUpperCase();
			strArr.set(i, word.trim());
		}
	}

	public static void parseAllLrcFromAFolder(File folder) throws IOException {
		File[] lrcList = folder.listFiles();
		HTree hashtable = DatabaseManager.getHashtableSingleton();
		for (File lrcFile : lrcList) {
			LrcParser lrcParser = new LrcParser(lrcFile.getName(), hashtable);
			lrcParser.performParse();
		}
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