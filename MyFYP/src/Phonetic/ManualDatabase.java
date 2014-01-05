package Phonetic;

import java.io.IOException;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class ManualDatabase {
	
	/*public static String getCV(String s) {
		String cv = "";
		for(int i = 0; i <s.length();++i){
			if(s.charAt(i) == 'A' ||s.charAt(i)=='E'||s.charAt(i)=='I'||s.charAt(i)=='O'||s.charAt(i)=='U'||s.charAt(i)=='Y')
				cv+= "v";
			else cv+= "c";
			}
		return cv;
	}*/

	public static void main(String[] args) throws IOException {
		
		RecordManager recman = RecordManagerFactory.createRecordManager("Dictionary");
		HTree hashtable;
		long recid = recman.getNamedObject("Dictionary");
		if(recid!=0) {
          	hashtable  = HTree.load(recman, recid);
          }
          else {
          	hashtable = HTree.createInstance(recman);
          	recman.setNamedObject("Dictionary", hashtable.getRecid());
          }
		hashtable.put("CHAINZ", new WordStruct("CHAINZ","01","ccvv*cv"));
		hashtable.put("PAYCATION", new WordStruct("PAYCATION","010","cvv*cv*cvvc"));
		hashtable.put("FAM", new WordStruct("FAM","1","cv*c"));
		hashtable.put("ONE-EYED", new WordStruct("ONE-EYED","01","cv*cvvv*c"));
		hashtable.put("KISSIN'", new WordStruct("KISSIN'","10","cv*ccv*c"));
		hashtable.put("'BOUT", new WordStruct("'BOUT","1","cvv*c")); 
		
		
		
		recman.commit();
		recman.close();
	}
}
