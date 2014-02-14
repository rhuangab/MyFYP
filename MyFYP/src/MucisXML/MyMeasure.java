package MucisXML;

import java.util.List;

public class MyMeasure {
	private List<MyNote> noteList;
	private int attr_division = 1;
	private String attr_clef_sign = "";
	private int attr_clef_line = 0;
	private int attr_key_fifths = 0;
	
	public MyMeasure(){
		noteList = null;
	}
	
	public MyMeasure(List<MyNote>TnoteList){
		noteList = TnoteList;
	}
	
	public void setDivision(int div){
		attr_division = div;
	}
	
	public void setClef(String sign, int line){
		attr_clef_sign = sign;
		attr_clef_line = line;
	}
	
	public void setKey(int fifth){
		attr_key_fifths = fifth;
	}
	
	public List<MyNote> getNotes(){
		return noteList;
		
	}
}
