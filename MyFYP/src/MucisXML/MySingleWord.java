package MucisXML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySingleWord {
	private List<MyNote> notes;
	private Iterator<MyNote> itr;
	private String word;
	public MySingleWord(MyNote note)
	{
		notes = new ArrayList<MyNote>();
		notes.add(note);
		word = note.getLyricContent().trim();
		itr = notes.iterator();
	}
	
	public List<MyNote> getNotes()
	{
		return notes;
	}
	
	public boolean hasNextNote()
	{
		return itr.hasNext();
	}
	
	public MyNote getNextNote()
	{
		if(itr.hasNext())
			return (MyNote) itr.next();
		return null;
	}
	
	public void addNote(MyNote note)
	{
		notes.add(note);
		word += note.getLyricContent().trim();
		itr = notes.iterator();
	}
	
	public int numOfSyllables()
	{
		return notes.size();
	}
	
	public String getWordText()
	{
		return word;
	}
	
	public void setWordText(String wordText)
	{
		word = wordText;
	}
}
