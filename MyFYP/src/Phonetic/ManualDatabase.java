package Phonetic;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import DatabaseManager.DatabaseManager;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class ManualDatabase {
	private HTree hashtable;
	public Scanner scanner;
	public ManualDatabase() throws IOException
	{
		hashtable = null;
		scanner = new Scanner(new File("log_toUpdate.txt"));
	}
	
	public void addAllWord() throws IOException
	{
		hashtable = DatabaseManager.getHashtableSingleton();
		while(scanner.hasNext())
		{
			addAWordToDatabase(getNextWordStruct());
		}
		DatabaseManager.commitAndClose();
	}
	
	public WordStruct getNextWordStruct()
	{
		if(scanner.hasNext())
		{
			String word = scanner.next().toUpperCase();
			String stress = scanner.next();
			String cv = scanner.next();
			return new WordStruct(word, stress, cv);
		}
		return null;
	}
	
	public void addAWordToDatabase(WordStruct wd) throws IOException
	{
		hashtable.put(wd.word, wd);
	}
	
	
	
	/*public static String getCV(String s) {
		String cv = "";
		for(int i = 0; i <s.length();++i){
			if(s.charAt(i) == 'A' ||s.charAt(i)=='E'||s.charAt(i)=='I'||s.charAt(i)=='O'||s.charAt(i)=='U'||s.charAt(i)=='Y')
				cv+= "v";
			else cv+= "c";
			}
		return cv;
	}*/

	public boolean isExist(String word) throws IOException
	{
		WordStruct wd = (WordStruct) hashtable.get(word);
		if(wd != null)
			System.out.println(wd.word+":\ncv:"+wd.cv+"\nStress:"+wd.stress);
		else
			System.out.println("Word not found");
		return wd != null;
	}
	
	public void addWord(String word,String stress, String cv,boolean hardOverwrite) throws IOException
	{
		if(hardOverwrite || !isExist(word))
		{
			hashtable.put(word, new WordStruct(word, stress, cv));
			DatabaseManager.commit();
			isExist(word);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		ManualDatabase md = new ManualDatabase();
		while(md.scanner.hasNext())
		{
			System.out.println(md.scanner.next());
		}
	}
}
