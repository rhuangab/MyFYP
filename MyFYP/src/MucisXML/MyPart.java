package MucisXML;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

public class MyPart {

	private List<MyMeasure> measureList;
	
	public MyPart(){
		measureList = null;
	}
	
	public MyPart(List<MyMeasure> TmeasureList){
		measureList = TmeasureList;
	}
	
	public void returnPattern(){
		for (Iterator i = measureList.iterator(); i.hasNext();) { 
			MyMeasure measure = (MyMeasure) i.next();
			
			for (Iterator j = measure.getNotes().iterator(); j.hasNext();) { 
				MyNote note = (MyNote) j.next();
				if (note.whetherWanted()){
					//System.out.print("[" + note.getLyricContent() + " ");
					System.out.println(note.getLyricContent() + ": " + note.getSyllable());
					//System.out.print(note.getPitchValue() + "]");
				}
			}
		}
	}
	
	public void returnLyricsByWords(){
		String iLyric = "";
		for (Iterator i = measureList.iterator(); i.hasNext();) { 
			
			MyMeasure measure = (MyMeasure) i.next();
			for (Iterator j = measure.getNotes().iterator(); j.hasNext();) { 
				
				MyNote note = (MyNote) j.next();
				if (note.whetherWanted()){
					if (note.getSyllable().equals("single"))
						System.out.print(note.getLyricContent() + " ");
					else if (note.getSyllable().equals("begin"))
						iLyric = note.getLyricContent();
					else if (note.getSyllable().equals("middle"))
						iLyric += note.getLyricContent();
					else if (note.getSyllable().equals("end")){
						iLyric += note.getLyricContent();
						System.out.print(iLyric + " ");
						iLyric = "";
					}
					else; // Exception handling
					//System.out.print(note.getLyricContent() + " ");
				}
			}
		}
	}
}
