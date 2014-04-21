package MucisXML;

public class MyPitch {
	private String step;
	private String octave;
	private int alter;
	
	public MyPitch() {
		step = "";
		octave = "0";
		alter = 0;
	}
	
	public MyPitch(String ste, String oct, int al) {
		step = ste;
		octave = oct;
		alter = al;
		
	}
	
	public String getMPitch(){
		return (step+octave);
	}
	
	public int getDigitalValue()
	{
		return 0;
	}
	
	public int getPitchValue()
	{
		String pitch = step+octave;
		int base[] = {9,11,0,2,4,5,7};
		int sharpBase[] = {10,-1,1,3,-1,6,8};
		int result = 0;
		if((pitch.charAt(0) == 'A' || pitch.charAt(0) == 'B') && pitch.substring(1).equals("10"))
			throw new NullPointerException();
		else if(pitch.charAt(0) >= 'A' && pitch.charAt(0) <= 'G')
		{
			result = base[pitch.charAt(0) - 'A'] + Integer.parseInt(pitch.substring(1))*12;
		}
		else{
			System.out.println(pitch);
			throw new NullPointerException();
		}
		if(result < 0) System.out.println("pitch value < 0");
		return result+alter;
	}
	
}
