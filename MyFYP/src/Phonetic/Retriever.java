package Phonetic;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

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
	
	/*public int getfirstEmp() {
		for(int i = 0;i < dataList.size(); i=i+2) {
			if(dataList.get(i).equals(m_type))
				return dataList.get(i).indexOf("ˈ");
		}
		return -1;
	}
	
	public int getsecondEmp() {
		for(int i = 0;i < dataList.size(); i=i+2) {
			if(dataList.get(i).equals(m_type))
				return dataList.get(i).indexOf("ˌ");
		}
		return -1;
	}*/
	
	public String getPhonetics() {
		return m_phonetic;
		
	}
	
	//partition the word according to syllables and rank all the syllables;para word is the string of phonetics
	public String partiton(String word){
		//System.out.println("debug: " + word);
		//if cannot decide part of speech
		if(word==null) 
			return "none";
		if(m_type.equals("undeter"))
			return "none";
		if(word.equals("none"))
			return "none";
		int america = word.indexOf(",");
		if(america > 0)
			word=word.substring(1,america);
		else word=word.substring(1,word.length()-1);
		//System.out.println("apple" + word);
		int[] pos = new int[10]; //vectors which keep the position of "-"
		int count=0;
		for(int i = 0;i<word.length();++i) {
			if(word.charAt(i)=='-') {
				pos[count++]=i;
			}
		}
		String rank=null;
		int firstEmp= word.indexOf("ˈ");
		int secondEmp = word.indexOf("ˌ");
		//System.out.println(firstEmp+"   "+ secondEmp + " " + count + " " +pos[count-1]);
		if(firstEmp!= -1 && firstEmp<pos[0]) {
			//System.out.println("case 1");
			rank="1";
		}
		else if(secondEmp!=-1 && secondEmp < pos[0]) {
			//System.out.println("case 2");
			rank="2";
			
			
		}
		else 
			rank="3";
		
		for(int i=1;i<count;++i) {
			//System.out.println(pos[i]);
			if(firstEmp!=-1 && firstEmp < pos[i] && firstEmp > pos[i-1]) {
				//System.out.println("case 3");
				rank=rank+"1";
			}
			else if(secondEmp!=-1 && secondEmp < pos[i] && secondEmp > pos[i-1]) {
				//System.out.println("case 4");
				rank=rank+"2";
			}
			else {
				//System.out.println("case 5");
				rank=rank+"3";
			}
		}
		if(count>=1) {
			if(firstEmp>pos[count-1])
				rank=rank+"1";
			else if(secondEmp>pos[count-1])
				rank=rank+"2";
			else
				rank=rank+"3";
		}
		
		return rank;
	}
	
	
	
	

	public Retriever(String word, String type) throws ParserException
	{
		dataList = new ArrayList();
		m_word = word;
		m_type=type;
		//1 is default value, meaning the first choice of a word with multiple meanings
		try{
		//m_type = type;
		retrieve();
		}
		catch(NullPointerException e)
		{
			//System.out.println("*Exception for catching phonetic for " + word + ".");
		}
		catch(ParserException e)
		{
			//System.out.println("*Exception for catching phonetic for " + word + ".");
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
		//System.out.println(m_word);
		boolean isPro = false;
		String previous = null;
		String temp;
		for(int i=0;i<typeNodes.size();i++)
		{
			temp = typeNodes.elementAt(i).toPlainTextString();
			//if it is POS
			if(isPro==false) {
				dataList.add(temp);
			}
			//if it should be phonetics
			//if it is indeed an phonetics
			else if(temp.contains("\\")) {
				if(temp.contains("same as")==false) {
				    dataList.add(temp);
				    previous=temp;
				}
			}
			//if it is not 
			else if(temp.contains("\\")==false){
				//System.out.println("case 3");
				dataList.add(previous);
				dataList.add(temp);
			}
			if(isPro==true) 
				isPro=false;
			else isPro=true;
		}
		for(int i=0;i<dataList.size();++i) {
			//System.out.println(dataList.get(i) + "  ");
			//here not very accurate
			if(dataList.get(i).contains(m_type) && i+1<dataList.size()) {
				m_phonetic = dataList.get(i+1);
			}
			/*
			if(i%2==1) {
				System.out.println(partiton(dataList.get(i)));
			}*/
		}
		if(m_phonetic == null) {
			m_phonetic = "none";
		}
		//System.out.println(m_phonetic);
		
	}
	
	
	/**
	 * @param args
	 * @throws ParserException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws ParserException, FileNotFoundException {
		// TODO Auto-generated method stub
		/*File file = new File("dictionary.txt");
		Scanner sc = new Scanner(file);
		while(sc.hasNext())
		{
				String word = sc.next();
				Retriever retriever = new Retriever(word,null);
		}*/
		  //retriever.extractWords("http://www.learnersdictionary.com/definition/hello");
		Retriever retriever = new Retriever("am","verb");
		System.out.println(retriever.getPhonetics());
		System.out.println(retriever.partiton(retriever.getPhonetics()));
	}

}
