package MucisXML;

public class MyNote {

	private MyPitch pitch;
	private String duration = "0";
	private String type = "quarter";
	private MyLyric lyric;
	private boolean IsWanted = false;
	private int rythm;
	
	public MyNote(){
	    pitch = new MyPitch();
	    lyric = new MyLyric();
	    rythm = 0;
	}
	
	public MyNote(String dur, String typ){
		pitch = new MyPitch();
	    lyric = new MyLyric();
		duration = dur;
		type = typ;
		rythm = 0;
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
	
	public int getRythm() {
		return rythm;
	}
	
	public void setRythm(int s){
		rythm = s;
	}
	
	public String getType()
	{
		return type;
	}
	
	
}
