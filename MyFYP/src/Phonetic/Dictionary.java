package Phonetic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;


public class Dictionary {
	
	public static boolean isVowel(String phone) {
		boolean judge = false;
		if(phone.equals("AA") ||phone.equals("AE") ||phone.equals("AH") ||phone.equals("AO") ||phone.equals("AW") ||phone.equals("AY")
				||phone.equals("EH") ||phone.equals("ER") ||phone.equals("EY")||phone.equals("IH")||phone.equals("IY") ||phone.equals("OW") 
				||phone.equals("OY"))
			judge = true;
		return judge;
			
	}
	
	public static void main(String[] args) throws IOException {
	
		Scanner s = null;
		String word;
		String phone;
		
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
          
		try {
			s = new Scanner(new BufferedReader(new FileReader("CMUDic.txt")));
			String line;
			int count = 0;
            
			while(s.hasNextLine()) {
				line = s.nextLine();
				String[] strArr1 = line.split(" ");
				List<String> strArr = new ArrayList();
				for(int i = 0; i < strArr1.length;++i) {
					if(!strArr1[i].equals(""))
						strArr.add(strArr1[i]);
				}
				
				
				/*for(int i = 0 ;i < strArr.size(); i++)
					System.out.print(strArr.get(i)+ " ");
				System.out.println();*/
				
				String stress = "";
				String cv = "";
				for(int i = 1; i < strArr.size(); ++i) {
					String temp = strArr.get(i); 
					int position = temp.length()-1;
				    char end = temp.charAt(position); 
					//if the phoneme ends with a number , then it is vowel and we can detect whether it is stress
					if(end >= '0' && end <='2') {
						stress += end;
						temp = temp.substring(0, position);
					}
					
					if(isVowel(temp)) {
						cv += "v";
					}
					else{
						cv += "c";
					}
					if(end >= '0' && end <='2')
						cv += "*";
					
				}
				//System.out.println(strArr.get(0) + "     "+ stress + "     " + cv);
				WordStruct wordStruct = new WordStruct(strArr.get(0),stress,cv);
				String tempWord = strArr.get(0);
				hashtable.put(tempWord, wordStruct);
				if(count%100 == 0) {
					recman.commit();
					count=-1;
				}
					
				count++;
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(s!=null) {
				s.close();
			}
		}
		
		FastIterator iter = hashtable.keys();
		String key;
		 while( (key = (String)iter.next())!=null)
         {
                 // get and print the content of each key
			 WordStruct myWord = (WordStruct) hashtable.get(key);
            System.out.println(key + " : " + myWord.word + "      "+  myWord.stress + "      "+  myWord.cv);
         }
		 
		 recman.commit();
		 recman.close();

	}

}
