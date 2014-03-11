package MucisXML;

public class MyPitch {
	private String step;
	private String octave;
	
	public MyPitch() {
		step = "";
		octave = "0";
	}
	
	public MyPitch(String ste, String oct) {
		step = ste;
		octave = oct;
	}
	
	public String getMPitch(){
		return (step+octave);
	}
	
	public int getDigitalValue()
	{
		return 0;
	}
}
