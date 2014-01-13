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
			String inputString = input.next();
			if(inputString.equals("richeng"))
			{
				DatabaseManager.close();
				break;
			}
			else if(inputString.charAt(0) == '_' && inputString.length() > 1)
			{
				deleteAWord(inputString.substring(1));
				continue;
			}
			WordStruct wd = (WordStruct)hashtable.get(inputString.toUpperCase());
			if(wd != null)
				System.out.println(wd.word+"\nStress: "+wd.stress+"\nCv: "+wd.cv);
			else
				System.out.println("Word not found");
		}
	}

}
