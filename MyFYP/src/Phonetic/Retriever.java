package Phonetic;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class Retriever {
	private String m_phonetic;
	private String m_word;
	private String m_type;
	private int firstEmphasis;
	private int secondEmphasis;
	private List<String> dataList;

	public Retriever(String word, String type) throws ParserException
	{
		dataList = new ArrayList();
		m_word = word;
		//1 is default value, meaning the first choice of a word with multiple meanings
		try{
		//m_type = type;
		retrieve();
		}
		catch(NullPointerException e)
		{
			System.out.println("*Exception for catching phonetic for " + word + ".");
		}
		catch(ParserException e)
		{
			System.out.println("*Exception for catching phonetic for " + word + ".");
		}
		/** use this data structure to store in the database */
		WordStruct wordStruct = new WordStruct(m_word,dataList);
	}
	/*
	public boolean isPhonetic(String input)
	{
		if(input != null && input.contains("/"))
			return true;
		else
			return false;
	}*/

	public void retrieve() throws ParserException,NullPointerException
	{
		String url = "http://www.merriam-webster.com/dictionary/" + m_word;
		Parser parser = new Parser();
		parser.setURL(url);
		//NodeList nodes = parser.extractAllNodesThatMatch(new TagNameFilter("span"));
		NodeFilter filter1 = new OrFilter(new HasAttributeFilter("class","main-fl"),new HasAttributeFilter("class","pr"));
		NodeFilter filter2 = new HasParentFilter(new HasAttributeFilter("id","headword"));
		NodeList typeNodes = parser.extractAllNodesThatMatch(new AndFilter(filter1, filter2));
		//NodeList phoneticNodes = parser.extractAllNodesThatMatch();
		System.out.println(m_word);
		for(int i=0;i<typeNodes.size();i++)
		{
			System.out.println(typeNodes.elementAt(i).toPlainTextString());
			dataList.add(typeNodes.elementAt(i).toPlainTextString());
			
			/*String currentContent = typeNodes.elementAt(i).toPlainTextString();
			if(type != null && type.equals(typeNodes.elementAt(i).toPlainTextString()))				
				if(isPhonetic(currentContent))
					previousPhonetic = currentContent;*/
		}
		
		/*
		//Node node = nodes.elementAt(1);
		Node parent = node.getParent();
		Node wordType = parent.getLastChild().getPreviousSibling();
		m_type = wordType.toPlainTextString();
		
		/** if the phonetic does not contain '/', meaning that we get the word type instead of phonetic.
		 * It is the second or latter meaning of whose phonetic is the same to the first meaning.
		 * For example, 'surprise', that has 3 meanings, but all three meaning hold the same phonetic
		 * And the second and third meanings do not list out the phonetic, so we use the old phonetic.  		
		*/
		/*
		if(node.toPlainTextString().contains("/"))
			m_phonetic = node.toPlainTextString();
		firstEmphasis = m_phonetic.indexOf("ˈ");
		secondEmphasis = m_phonetic.indexOf("ˌ");
		System.out.println(m_word);
		System.out.println(m_phonetic);
		System.out.println("First emphasis: "+m_phonetic.indexOf("ˈ"));
		System.out.println("Second emphasis: "+m_phonetic.indexOf("ˌ"));
		System.out.println("Type: "+m_type);*/
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
