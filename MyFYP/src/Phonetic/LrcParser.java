package Phonetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DatabaseManager.DatabaseManager;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class LrcParser {

	private String filename;
	private Scanner s;
	private FileWriter phone;
	private FileWriter cv;
	private FileWriter log;
	private String songName;
	private HTree hashtable;
	private PriorityQueue<String> priorityQueue;
	private Pattern p;

	public LrcParser(String sn) throws IOException {
		songName = sn;		
		hashtable = DatabaseManager.getHashtableSingleton();
		phone = new FileWriter("phoneLibrary/" + songName + ".phone.txt");
		cv = new FileWriter("cvLibrary/" + songName + ".cv.txt");
		log = new FileWriter("log.txt",true);
		s = new Scanner(new BufferedReader(new FileReader(
				"lrcLibrary/"+songName+".lrc")));
		p = Pattern.compile("((\\[\\d{2}:\\d{2}\\.\\d{1,2}\\])((\\[\\d{2}:\\d{2}\\.\\d{1,2}\\])*))(.+)");
	}
	
	

	public void performParse() throws IOException {
		String line;
		//regular expression
		
		while (s.hasNextLine()) {
			// System.out.println(s.nextLine());
			line = s.nextLine();
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
							log.write(strArr.get(i) + "    ");
							log.write("\n");
							phone.write("UNDEF" + " ");
							cv.write("UNDEF" + "  ");
							// System.out.println(strArr.get(i) +
							// " is not in the dictionary");
						}
					}
					phone.write("\n");
					cv.write("\n");
				}

				System.out.print(timestamp);
				for (int i = 0; i < strArr.size(); ++i) {
					System.out.print("[" + strArr.get(i) + "]");
				}
				System.out.println();

			}
		}

		if (s != null)
			s.close();
		if (phone != null)
			phone.close();
		if (cv != null)
			cv.close();
		if (log != null)
			log.close();

		// recman.commit();
		DatabaseManager.commitAndClose();
	}

	// judge if a a character is a punctuation
	public static String removePunc(String s) {
		s = s.replace('!', '\0');
		s = s.replace('?', '\0');
		s = s.replace(',', '\0');
		s = s.replace('.', '\0');
		s = s.replace('(', '\0');
		s = s.replace(')', '\0');

		return s;
	}

	public static String expandNum(String s) {

		if (s.contains("0"))
			s = s.replace("0", " ZERO ");
		else if (s.contains("1"))
			s = s.replace("1", " ONE ");
		else if (s.contains("2"))
			s = s.replace("2", " TWO ");
		else if (s.contains("3"))
			s = s.replace("3", " THREE ");
		else if (s.contains("4"))
			s = s.replace("4", " FOUR ");
		else if (s.contains("5"))
			s = s.replace("5", " FIVE ");
		else if (s.contains("6"))
			s = s.replace("6", " SIX ");
		else if (s.contains("7"))
			s = s.replace("7", " SEVEN ");
		else if (s.contains("8"))
			s = s.replace("8", " EIGHT ");
		else if (s.contains("9"))
			s = s.replace("9", " NINE ");
		return s;

	}

	// remove some abbreviation(including possessive, abbreviation for am,
	// is,have and so on)
	public static String removeAbb(String s) {
		s = s.replace("'s", "");
		s = s.replace("'ve", "");
		s = s.replace("'m", "");
		s = s.replace("'ll", "");
		return s;
	}

	// preprocess one: capitalize every letter, remove possessive and
	// abbreviation('s, 'm, 've, '), and all the punctuation(one time one
	// sentence)
	public static void preprocessOne(List<String> strArr) {
		// ArrayList<String> newList = new ArrayList<String>();
		String word;
		for (int i = 0; i < strArr.size(); ++i) {
			word = strArr.get(i);
			word = removePunc(word);
			//word = removeAbb(word);
			word = word.toUpperCase();
			strArr.set(i, word.trim());
		}
	}
	
	public void parseAllLrcFromAFolder(File folder) throws IOException
	{
		File[] lrcList = folder.listFiles();
		for(File lrcFile : lrcList)
		{
			LrcParser lrcParser = new LrcParser(lrcFile.getName());
			lrcParser.performParse();
		}
	}

	public static void main(String[] args) throws IOException {
		LrcParser lrcParser = new LrcParser("A MOMENT LIKE THIS");
		//lrcParser.sortTimeStamp();
	}

}