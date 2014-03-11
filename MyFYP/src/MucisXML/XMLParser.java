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

import FileLibraryPath.FileLibraryPath;

public class XMLParser {
	private List<MyPart> myParts;
	private File xmlFile;
	private int beatRythm[];

	// private List<String> aaaaa;

	public XMLParser(File xml) throws Exception {
		// beatRythm[8] = {4,
		// aaaaa = new ArrayList<String>();
		// aaaaa.add("whole");aaaaa.add("half");aaaaa.add("quarter");aaaaa.add("eighth");aaaaa.add("16th");aaaaa.add("32nd");aaaaa.add("64th");
		myParts = new ArrayList<MyPart>();
		xmlFile = xml;
		if (xmlFile == null)
			xmlFile = new File(
					"/Users/apple/Dropbox/midi file (2)/bucket1/93 MILLION MILES.xml");
		parse();
		// part.returnPattern();
		// getLyricAsWords();

	}

	public String getLyricAsWords() {
		String output = "";
		for (MyPart aPart : myParts) {
			aPart.returnPattern();
			output += aPart.returnLyricsByWords() + '\n';
		}
		return output;
	}

	public void parse() throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(xmlFile);

		Element rootElm = document.getRootElement();
		// List nodes = rootElm.elements("part");
		// System.out.println(partsWithLyrics.size());
		// System.out.println(((Element)partsWithLyrics.get(1)).attributeValue("id"));

		// Judge the main part

		/*
		 * for (Iterator a = nodes.iterator(); a.hasNext();) { Element j_part =
		 * (Element) a.next(); List judgeMeasures = j_part.elements("measure");
		 * 
		 * for (Iterator b = judgeMeasures.iterator(); b.hasNext();) { Element
		 * j_measure = (Element) b.next(); List judgeNotes =
		 * j_measure.elements("note"); for (Iterator c = judgeNotes.iterator();
		 * c.hasNext();) { Element j_note = (Element) c.next();
		 * 
		 * if (j_note.element("lyric") != null){ if (myIndex != mainIndex &&
		 * mainIndex != -1) throw new
		 * Exception("There are multiple parts that contains lyrics!"); else
		 * mainIndex = myIndex; } } } myIndex++; }
		 */
		List partsWithLyrics = document.selectNodes("//lyric/ancestor::part");
		for (Iterator itr = partsWithLyrics.iterator(); itr.hasNext();) {
			Element mainPart = (Element) itr.next();
			int beats = Integer.parseInt(((Element) mainPart.selectNodes(
					"//beats").get(0)).getText());
			int beatsType = Integer.parseInt(((Element) mainPart.selectNodes(
					"//beat-type").get(0)).getText());
			int stepSize = 64 / beatsType;
			int maxTotal = stepSize * beats;
			/*
			 * if(beats != null && beatsType!= null &&
			 * !(beats.get(0).getText().equals
			 * ("4")&&beatsType.get(0).getText().equals("4"))) {
			 * System.out.println(xmlFile.getName());
			 * System.out.println(beats.size() + " "+beatsType.size() + " " +
			 * beats.get(0).getText() + " " + beatsType.get(0).getText()); }
			 */

			List<MySingleWord> wordList = new ArrayList<MySingleWord>();
			List MeasureNodes = mainPart
					.selectNodes("//lyric/ancestor::measure");
			MySingleWord singleWord = null;
			for (Iterator i = MeasureNodes.iterator(); i.hasNext();) {
				int curTotal = 0;
				Element e_measure = (Element) i.next();
				List NoteNodes = e_measure.elements("note");
				// List<MyNote> noteList = new ArrayList<MyNote>();
				for (Iterator j = NoteNodes.iterator(); j.hasNext();) {
					Element e_note = (Element) j.next();
					MyNote note = new MyNote(e_note.element("duration")
							.getText(), e_note.element("type").getText());
					//System.out.println(xmlFile.getName() + " " +mainPart.attributeValue("id")+" "+ e_measure.attributeValue("number"));
					//System.out.println(String.format("%d+%d+%d", beatsType,beats,(curTotal/stepSize)+1));
					//temp
					if(curTotal > maxTotal-1)
						curTotal = maxTotal-1;
					note.setRythm(FileLibraryPath.beatsMap.get(String.format("%d+%d+%d", beatsType,beats,(curTotal/stepSize)+1)));
					curTotal += FileLibraryPath.stepMap.get(note.getType());
					if(e_note.element("dot") != null) curTotal += FileLibraryPath.stepMap.get(note.getType())/2;
					
					boolean isPitch = false;
					MyPitch pitch = null;
					if (e_note.element("pitch") != null) {
						pitch = new MyPitch(e_note.element("pitch")
								.element("step").getText(), e_note
								.element("pitch").element("octave").getText());
						isPitch = true;
					}
					note.setPitch(pitch);
					// if(!aaaaa.contains(note.getType()))
					// System.out.println(note.getType());
					// if(note.getType().equals("64th") &&
					// e_note.element("dot")!= null)
					// System.out.println("XXXXX");
					// if(note.getType()!= null && note.getType().equals("16th")
					// && e_note.element("dot") !=null)
					// System.out.println("XXXXX");
					boolean isLyric = false;
					MyLyric lyric = null;
					if (e_note.element("lyric") != null) {
						lyric = new MyLyric(
								e_note.element("lyric").element("syllabic")
										.getText(),
								e_note.element("lyric")
										.element("text")
										.getText()
										.replaceAll(
												"\"|\\,|\\.|\\?|\\!|\\~|\\(|\\;|\\-|\\)",
												"").replace("in'", "ing"));
						isLyric = true;
						note.setLyric(lyric);
						if (isPitch && isLyric) {
							note.setWanted();

							String syllabic = e_note.element("lyric")
									.element("syllabic").getText();
							if (syllabic.equals("single")) {
								wordList.add(new MySingleWord(note));
							} else if (syllabic.equals("begin")) {
								singleWord = new MySingleWord(note);
							} else if (syllabic.equals("middle")) {
								singleWord.addNote(note);
							} else // if(syllabic.equals("end"))
							{
								singleWord.addNote(note);
								wordList.add(singleWord);
								singleWord = null;
							}
						}
					}
				}
			}
			myParts.add(new MyPart(wordList));
		}
	}

	public List<MyPart> getAllParts() {
		return myParts;
	}

	public static void main(String[] args) throws Exception {
		XMLParser xmlParser = new XMLParser(null);
		System.out.println(xmlParser.getLyricAsWords());
	}

}
