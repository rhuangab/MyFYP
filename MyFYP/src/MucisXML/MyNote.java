package MucisXML;

public class MyNote {

	private MyPitch pitch;
	private int duration;
	private String type = "quarter";
	private MyLyric lyric;
	private boolean IsWanted = false;
	private int rythm;
	
	public MyNote(){
	    pitch = new MyPitch();
	    lyric = new MyLyric();
	    rythm = 0;
	}
	
	public MyNote(String typ){
		pitch = new MyPitch();
	    lyric = new MyLyric();
		duration = 0;
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
	
	public void setDur(int dur)
	{
		duration = dur;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public String getLyricContent(){
		return lyric.getLyric();
	}
	
	public int getPitchValue(){
		return pitch.getPitchValue();
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
