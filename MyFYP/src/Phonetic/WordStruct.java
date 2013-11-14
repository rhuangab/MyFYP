package Phonetic;

import java.io.Serializable;
import java.util.List;

public class WordStruct implements Serializable{
	public String word;
	//public String phonetic;
	//public String type;
	public List<String> dataList;
	//public int firstEmphasis;
	//public int secondEmphasis;

	WordStruct(String word, List<String> dataList/*String phonetic, String type, int fE, int sE*/)
	{
		this.word = word;
		this.dataList = dataList;
		//this.phonetic = phonetic;
		//this.type = type;
		//this.firstEmphasis = fE;
		//this.secondEmphasis = sE;
	}
}
