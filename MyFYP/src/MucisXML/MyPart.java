package MucisXML;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Element;

public class MyPart {

	private List<MySingleWord> wordList;
	private Iterator<MySingleWord> itr;
	private Iterator<MySingleWord> itr2;
	private MySingleWord currentWord;
	
	public MyPart(List<MySingleWord> TwordList){
		wordList = TwordList;
		itr = wordList.iterator();
		itr2 = wordList.iterator();
	}
	
	public MySingleWord getNextWord()
	{
		if(itr.hasNext())
			return itr.next();
		return null;
	}
	
	public MyNote getNextNote()
	{
		if((currentWord == null || !currentWord.hasNextNote())&& itr2.hasNext())
		{
			currentWord = itr2.next();
		}
		else if((currentWord == null || !currentWord.hasNextNote())&& !itr2.hasNext()) return null;
		else if(currentWord.hasNextNote())
			return currentWord.getNextNote();
		return null;
	}
	
	
	public void returnPattern(){
		for (Iterator i = wordList.iterator(); i.hasNext();) { 
			MySingleWord singleWord = (MySingleWord) i.next();
			
			for (Iterator j = singleWord.getNotes().iterator(); j.hasNext();) { 
				MyNote note = (MyNote) j.next();
				if (note.whetherWanted()){
					//System.out.print("[" + note.getLyricContent() + " ");
					System.out.println(note.getLyricContent() + ": " + note.getSyllable());
					//System.out.print(note.getPitchValue() + "]");
				}
			}
		}
	}
	
	public String returnLyricsByWords(){
		String output = "";
		String iLyric = "";
		for (Iterator i = wordList.iterator(); i.hasNext();) { 
			
			MySingleWord singleWord = (MySingleWord) i.next();
			output += singleWord.getWordText() + " ";
			/*for (Iterator j = singleWord.getNotes().iterator(); j.hasNext();) { 
				
				MyNote note = (MyNote) j.next();
				if (note.whetherWanted()){
					if (note.getSyllable().equals("single"))
						output += note.getLyricContent() + " ";
					else if (note.getSyllable().equals("begin"))
						iLyric = note.getLyricContent();
					else if (note.getSyllable().equals("middle"))
						iLyric += note.getLyricContent();
					else if (note.getSyllable().equals("end")){
						iLyric += note.getLyricContent();
						output += iLyric + " ";
						iLyric = "";
					}
					else; // Exception handling
					//System.out.print(note.getLyricContent() + " ");
				}
			}*/
		}
		return output;
	}
}
