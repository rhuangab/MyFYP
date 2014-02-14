package MucisXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;


public class XMLParser {

	public static void main(String[] args) throws Exception { 
	
		SAXReader reader = new SAXReader();  
		Document document = reader.read(new File("C:\\Users\\lenovo\\Desktop\\PAYPHONE1.xml"));  

		Element rootElm = document.getRootElement(); 
		List nodes = rootElm.elements("part");
		
		//Judge the main part

		int mainIndex = -1;
		int myIndex = 0;
		for (Iterator a = nodes.iterator(); a.hasNext();) { 
			Element j_part = (Element) a.next();
			List judgeMeasures = j_part.elements("measure");
			
			for (Iterator b = judgeMeasures.iterator(); b.hasNext();) {  
				Element j_measure = (Element) b.next();  
				List judgeNotes = j_measure.elements("note");
				for (Iterator c = judgeNotes.iterator(); c.hasNext();) {  
					Element j_note = (Element) c.next();  
					if (j_note.element("lyric") != null){
						if (myIndex != mainIndex && mainIndex != -1)
							throw new Exception("There are multiple parts that contains lyrics!");
						else
							mainIndex = myIndex;
					}
				}
			}
			myIndex++;
		}
		
		Element mainPart = (Element) nodes.get(mainIndex);
		List<MyMeasure> measureList = new ArrayList<MyMeasure>();
		List MeasureNodes = mainPart.elements("measure");
		for (Iterator i = MeasureNodes.iterator(); i.hasNext();) {  
			Element e_measure = (Element) i.next();  
			List NoteNodes = e_measure.elements("note");
			List<MyNote> noteList = new ArrayList<MyNote>();
			for (Iterator j = NoteNodes.iterator(); j.hasNext();) { 
				Element e_note = (Element) j.next();  
				MyNote note = new MyNote(e_note.attributeValue("duration"), e_note.attributeValue("type"));
				
				boolean isPitch = false;
				MyPitch pitch = null;
				if (e_note.element("pitch") != null){
					pitch = new MyPitch(e_note.element("pitch").element("step").getText(),e_note.element("pitch").element("octave").getText());
					isPitch = true;
				}
				note.setPitch(pitch);
				
				boolean isLyric = false;
				MyLyric lyric = null;
				if (e_note.element("lyric") != null){
					lyric = new MyLyric(e_note.element("lyric").element("syllabic").getText(),e_note.element("lyric").element("text").getText());
					isLyric = true;
				}
				note.setLyric(lyric);
				if (isPitch && isLyric){
					note.setWanted();
				}
				noteList.add(note);
			}
			MyMeasure measure = new MyMeasure(noteList);
			measureList.add(measure);
		}
		
		MyPart part = new MyPart(measureList);
		//part.returnPattern();
		part.returnLyricsByWords();
	}
	
	

}
