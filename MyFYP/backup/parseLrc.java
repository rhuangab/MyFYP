package Phonetic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

import org.htmlparser.util.ParserException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class parseLrc {

    static String title;
	static String artist;
	static String album;
	String inputfilename;
	String outputfilename;
	static Vector<Vector<String> > timeList; //used for storing time for each lyrics
	static Vector<String> empList;//contains emphasis information for each sentence
	static TokenizerModel model;//models for tokenizer
	static Tokenizer tokenizer; 
	static POSModel model2;//models for part of speech
	static Vector<String> digital;//result 
	
	//convert representation of part of speech
	public static String convert(String standard) {
		if(standard.equals("NN") || standard.equals("NNS") || standard.equals("NNP") || standard.equals("NNPS"))
			return "noun";
		else if(standard.equals("VB")||standard.equals("VBD")||standard.equals("VBG")
				||standard.equals("VBN")||standard.equals("VBP")||standard.equals("VBZ"))
			return "verb";
		else if(standard.equals("JJ")||standard.equals("JJR")||standard.equals("JJS"))
			return "adjective";
		else if(standard.equals("RB")||standard.equals("RBR")||standard.equals("RBS")||standard.equals("WRB"))
			return "adverb";
		else if(standard.equals("IN"))
			return "preposition";
		else if(standard.equals("UH"))
			return "interjection";
		else if(standard.equals("WP")||standard.equals("WP$")||standard.equals("PRP")||standard.equals("PRP$"))
			return "pronoun";
		else if(standard.equals("CC"))
			return "conjunction";
		return "undeter";
		}
	
   //used for getting information for getting title, artist, album and time
	public static String getinfoTag(String tag, boolean isTime) {
		String info=null;
		
		if(isTime==false) {
			int pos = tag.indexOf(":");
			info = tag.substring(pos+1,tag.length()-1);
			}
		return info;
	}
	
	//expand abbreviation
	public static String expandabb(String abb) {
		if(abb.equals("'m"))
			return "am";
		else if(abb.equals("'s"))
			return "is";
		else if(abb.equals("'m"))
			return "am";
		else if(abb.equals("'Cause"))
			return "because";
		else if(abb.equals("'ll"))
			return "will";
		
		return null;
	}
	
	//check if the content is a word rather than a punctuation
	static boolean checkPun(String word) {
		if(word.equals("(") || word.equals(")")||word.equals(",")||word.equals(".")||word.equals("?")
			||word.equals("\""))
			return true;
		else return false;
		
	}
	
	public static void main(String[] args) throws ParserException {
		File lrcFile = new File("/Users/jenny/Documents/workspace/MyFYP/theClimb.txt");
		String line;
		try {
			Scanner scanner = new Scanner(lrcFile);
			line = scanner.nextLine();
			title = getinfoTag(line,false);
			
			line = scanner.nextLine();
			artist = getinfoTag(line,false);
			
			line = scanner.nextLine();
			album = getinfoTag(line,false);
			//establish models for tokenizer
			InputStream modelIn = new FileInputStream("en-token.bin");

			try {
			  model = new TokenizerModel(modelIn);
			  tokenizer = new TokenizerME(model);
			}
			catch (IOException e) {
			  e.printStackTrace();
			}
			finally {
			  if (modelIn != null) {
			    try {
			      modelIn.close();
			    }
			    catch (IOException e) {
			    }
			  }
			}
			
			//establish models for part of speech
			InputStream modelIn2 = null;

			try {
			  modelIn2 = new FileInputStream("en-pos-maxent.bin");
			  model2 = new POSModel(modelIn2);
			}
			catch (IOException e) {
			  // Model loading failed, handle the error
			  e.printStackTrace();
			}
			finally {
			  if (modelIn != null) {
			    try {
			      modelIn.close();
			    }
			    catch (IOException e) {
			    }
			  }
			}
						
			POSTaggerME tagger = new POSTaggerME(model2);
			
				
			while(scanner.nextLine() == "") {
			}
			timeList = new Vector<Vector<String> >();
			String tokens[];
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				//System.out.println(line);
				int pos=line.lastIndexOf("]");
				String tempTime=line.substring(0,pos+1);
				Vector<String> tempVec = new Vector<String>();
				int tempPos=tempTime.indexOf("]");
				while(tempTime.contains("]")) {
					tempPos=tempTime.indexOf("]");
					tempVec.addElement(tempTime.substring(1,tempPos));
					//tempVec.add(tempTime.substring(1,tempPos));
					//System.out.println(tempTime.substring(1,tempPos));
					tempTime=tempTime.substring(tempPos+1);
				}
				timeList.addElement(tempVec);
				
				String sentence = line.substring(pos+1);
				Vector<String> wordList = new Vector<String>();
				//System.out.println(sentence);
				//first separate each sentence into words
				tokens = tokenizer.tokenize(sentence);
				for(int i=0; i <tokens.length;i++) {
					if(tokens[i].substring(0, 1).equals("'") ) {
						tokens[i]=expandabb(tokens[i]);
					}
				}
				/*
                String tags[] = tagger.tag(tokens);
				for(int i = 0;i <tokens.length;++i) {
					System.out.print(tokens[i] + " "+tags[i]+ "   ");
				}
				System.out.println();*/
				String tags[] = tagger.tag(tokens);
				/*
				for(int i = 0;i< tokens.length; ++i) {
					if(checkPun(tokens[i])==false) {
						System.out.print(tokens[i] + "  "+tags[i] +" ");
					}
				}
				System.out.println();*/
				digital = new Vector<String>();
				String sen = "";
				Retriever retriever;
				String partofspeech;
				for(int i=0;i<tokens.length;++i) {
					if(checkPun(tokens[i])==false) {
						partofspeech=convert(tags[i]);
						System.out.print(tokens[i] + " "+ partofspeech+ "  ");
						retriever = new Retriever(tokens[i],partofspeech);
						if(retriever!=null) {
							//System.out.println(retriever.getPhonetics());
							sen=sen+retriever.partiton(retriever.getPhonetics())+"  ";
						}
					}
				}
				System.out.println();
				System.out.println(sen);
				
			}
			
			
			
			
			
			
			
			//System.out.println(title + " " + artist + " "+album);
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
