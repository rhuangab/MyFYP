package Phonetic;

import java.io.Serializable;

public class WordStruct implements Serializable{
	public String word;
	public String phonetic;
	public String type;
	public int firstEmphasis;
	public int secondEmphasis;

	WordStruct(String word, String phonetic, String type, int fE, int sE)
	{
		this.word = word;
		this.phonetic = phonetic;
		this.type = type;
		this.firstEmphasis = fE;
		this.secondEmphasis = sE;
	}
}
