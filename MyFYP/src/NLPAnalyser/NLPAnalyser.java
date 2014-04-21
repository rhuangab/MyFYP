package NLPAnalyser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import MucisXML.XMLParser;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class NLPAnalyser {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		POSModel model = new POSModelLoader().load(new File("/Users/apple/git/MyFYP/MyFYP/nlpModel/en-pos-maxent.bin"));
	    PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	    POSTaggerME tagger = new POSTaggerME(model);

	    String input = "Can anyone help me dig through OpenNLP's horrible documentation?";
	    ObjectStream<String> lineStream =
	            new PlainTextByLineStream(new StringReader(new XMLParser(null).getLyricAsWords()));

	    perfMon.start();
	    String line;
	    while ((line = lineStream.read()) != null) {

	        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
	        String[] tags = tagger.tag(whitespaceTokenizerLine);
	        for(int i=0; i<tags.length;i++)
	        	System.out.println(whitespaceTokenizerLine[i] + " : "+tags[i]);
	        POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
	        System.out.println(sample.toString());

	        perfMon.incrementCounter();
	    }
	    //perfMon.stopAndPrintFinalResult();

	}

}
