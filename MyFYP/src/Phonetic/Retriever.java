package Phonetic;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class Retriever {
	private String m_phonetic;
	private String m_word;
	private String m_type;
	private int firstEmphasis;
	private int secondEmphasis;

	public Retriever(String word, String type) throws ParserException
	{
		int i = 1;
		m_word = word;
		//1 is default value, meaning the first choice of a word with multiple meanings
		try{
		retrieve(i);
		//m_type = type;
		while(type != null && !m_type.equals(type))
		{
			retrieve(++i);
		}
		}
		catch(NullPointerException e)
		{
			System.out.println("*Exception for catching phonetic for " + word + " with type "+type+".");
		}
		/** use this data structure to store in the database */
		WordStruct wordStruct = new WordStruct(m_word,m_phonetic,m_type, firstEmphasis, secondEmphasis);
		
	}

	public void retrieve(int i) throws ParserException,NullPointerException
	{
		/*final WebClient webClient = new WebClient();
		//HtmlPage page = webClient.getPage("http://www.merriam-webster.com/dictionary/"+m_word);
		HtmlPage page = webClient.getPage("http://www.learnersdictionary.com/definition/hello");
		HtmlDivision headword = (HtmlDivision) page.getElementById("headword");
		System.out.println(headword.getChildElementCount());*/
		/*DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream io = new URL("http://www.merriam-webster.com/dictionary/hello").openStream();
		Document doc = dBuilder.parse(io);
		Element e = doc.getElementById("headword");
		System.out.println("1"+e.getNodeType());*/
		
		//String s = dBuilder.parse("http://www.merriam-webster.com/dictionary/hello").toString();
		//doc.getElementById(elementId)
		//HtmlDivision headword = (HtmlDivision) doc.getElementById("headword");
		//System.out.println(headword.getChildElementCount());
		//URLConnection uc = new URL("http://www.merriam-webster.com/dictionary/hello").openConnection();
		
		//HtmlPage htmlPage = new HtmlPage(parser);
		//System.out.println((htmlPage.getTitle()));
		//Node node = (Node)parser.extractAllNodesThatMatch(new TagNameFilter ("title")).elementAt(0);
		//org.htmlparser.filters.CssSelectorNodeFilter
		
		// String b = wordType.toPlainTextString();
		/*
		 * NodeList children = parent.getChildren(); for(int i =0; i <
		 * children.size();i++) {
		 * System.out.println(i+": "+children.elementAt(i).toPlainTextString()); }
		 */
		// System.out.println(.toPlainTextString());
		// System.out.println(b);
		// System.out.println(a.contains("\u0712"));
		// System.out.println((int)(a.charAt(1)));
		// System.out.println("\u7686");
		// System.out.println(node.toPlainTextString());
		String url = "http://www.learnersdictionary.com/definition/" + m_word;
		if(i > 1)
			url += "["+i+"]";
		Parser parser = new Parser();
		parser.setURL(url);
		NodeList nodes = parser.extractAllNodesThatMatch(new TagNameFilter("span"));
		Node node = nodes.elementAt(1);
		Node parent = node.getParent();
		Node wordType = parent.getLastChild().getPreviousSibling();
		m_type = wordType.toPlainTextString();
		
		/** if the phonetic does not contain '/', meaning that we get the word type instead of phonetic.
		 * It is the second or latter meaning of whose phonetic is the same to the first meaning.
		 * For example, 'surprise', that has 3 meanings, but all three meaning hold the same phonetic
		 * And the second and third meanings do not list out the phonetic, so we use the old phonetic.  		
		*/
		if(node.toPlainTextString().contains("/"))
			m_phonetic = node.toPlainTextString();
		firstEmphasis = m_phonetic.indexOf("ˈ");
		secondEmphasis = m_phonetic.indexOf("ˌ");
		System.out.println(m_word);
		System.out.println(m_phonetic);
		System.out.println("First emphasis: "+m_phonetic.indexOf("ˈ"));
		System.out.println("Second emphasis: "+m_phonetic.indexOf("ˌ"));
		System.out.println("Type: "+m_type);
	}
	
	
	/**
	 * @param args
	 * @throws ParserException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws ParserException, FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("dictionary.txt");
		Scanner sc = new Scanner(file);
		while(sc.hasNext())
		{
			String word = sc.next();
			Retriever retriever = new Retriever(word,null);
		}
		//retriever.extractWords("http://www.learnersdictionary.com/definition/hello");
	}

}
