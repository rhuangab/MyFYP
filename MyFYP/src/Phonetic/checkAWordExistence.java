package Phonetic;

import java.io.IOException;
import java.util.Scanner;

import jdbm.htree.HTree;
import DatabaseManager.DatabaseManager;

public class checkAWordExistence {

	public static void deleteAWord(String word) throws IOException
	{
		DatabaseManager.getHashtableSingleton().remove(word.toUpperCase());
		DatabaseManager.commit();
	}
	
	public static void addAWord(String wordAndValue) throws IOException
	{
		String[] words = wordAndValue.split("\\s+");
		if(words.length == 3)
		{
			WordStruct wd = new WordStruct(words[0].toUpperCase(), words[1], words[2]);
			DatabaseManager.getHashtableSingleton().put(words[0].toUpperCase(), wd);
			DatabaseManager.commit();
		}
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HTree hashtable = DatabaseManager.getHashtableSingleton();
		Scanner input = new Scanner(System.in);
		while(true)
		{
			System.out.print("Input a word: ");
			String inputString = input.nextLine();
			if(inputString.equals("end"))
			{
				DatabaseManager.close();
				break;
			}
			else if(inputString.charAt(0) == '-' && inputString.length() > 1)
			{
				deleteAWord(inputString.substring(1));
				continue;
			}
			else if(inputString.charAt(0) == '+' && inputString.length() > 1)
			{
				addAWord(inputString.substring(1));
				continue;
			}
				
			WordStruct wd = (WordStruct)hashtable.get(inputString.toUpperCase());
			if(wd != null)
				System.out.println(wd.word+" "+wd.stress+" "+wd.cv);
			else
				System.out.println("Word not found");
		}
	}

}
