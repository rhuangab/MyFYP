package MucisXML;

public class MyLyric{
	private String syllabic = "";
	private String text = "";
	
	public MyLyric() {
		syllabic = text = "";
	}
	
	public MyLyric(String syll, String tex){
		syllabic = syll;
		text = tex;
	}
	
	public String getLyric(){
		return text;
	}
	
	public String getSyllabic(){
		return syllabic;
	}
}
