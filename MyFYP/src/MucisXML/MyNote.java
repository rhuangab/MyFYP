package MucisXML;

public class MyNote {

	private MyPitch pitch;
	private String duration = "0";
	private String type = "quarter";
	private MyLyric lyric;
	private boolean IsWanted = false;
	
	public MyNote(){
	    pitch = new MyPitch();
	    lyric = new MyLyric();
	}
	
	public MyNote(String dur, String typ){
		duration = dur;
		type = typ;
	}
	
	public void setPitch(MyPitch Tpitch){
		pitch = Tpitch;
	}
	
	public void setLyric(MyLyric Tlyric){
		lyric = Tlyric;
	}
	
	public void setWanted(){
		IsWanted = true;
	}
	
	public boolean whetherWanted(){
		return IsWanted;
	}
	
	public String getLyricContent(){
		return lyric.getLyric();
	}
	
	public String getPitchValue(){
		return pitch.getMPitch();
	}
	
	public String getSyllable(){
		return lyric.getSyllabic();
	}
	
	
	
}
