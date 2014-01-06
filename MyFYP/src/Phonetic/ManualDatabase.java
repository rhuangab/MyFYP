package Phonetic;

import java.io.IOException;

import DatabaseManager.DatabaseManager;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class ManualDatabase {
	private HTree hashtable;
	public ManualDatabase() throws IOException
	{
		hashtable = DatabaseManager.getHashtableSingleton();
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

	}
}
