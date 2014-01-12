package Phonetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimestampComparator implements Comparator<String> {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws ParseException, FileNotFoundException {
		// TODO Auto-generated method stub
		
		//System.out.println(parsedData.compareTo(parsedData2));
		/*Scanner sc = new Scanner(new File("/Users/apple/Downloads/song/lrc_midi_combine/lrc/A MOMENT LIKE THIS.lrc"));
		Pattern pattern  = Pattern.compile("((\\[\\d{1,2}:\\d{1,2}\\.\\d{1,2}\\])((\\[\\d{2}:\\d{2}\\.\\d{1,2}\\])*))(.*)");
		while(sc.hasNext())
		{
		
		Matcher m = pattern.matcher(sc.nextLine());
		while(m.matches())
		{
			System.out.println(m.group(2)+m.group(5));
			m = pattern.matcher(m.group(3)+m.group(5));
		}
		}*/
	}

	@Override
	public int compare(String o1, String o2) {
		if(o1 == null && o2 == null)
			return 0;
		if(o1 == null)
			return -1;
		else if(o2 == null)
			return 1;
		
		int result = 0;
		
		try {
			result = extractTimestamp(o1).compareTo(extractTimestamp(o2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	private Date extractTimestamp(String input) throws ParseException
	{
		Pattern pattern  = Pattern.compile("(\\[\\d{1,2}:\\d{1,2}\\.\\d{1,2}\\])(.*)");
		Matcher m = pattern.matcher(input);
		//for example "[02:10.13]hello" ,m.group(1) will get "[02:10.13]"
		if(m.matches())
		{
			java.util.Date parsedData;
			SimpleDateFormat sdf = new SimpleDateFormat("[mm:ss.SS]");
			//try{
				parsedData =  sdf.parse(m.group(1));
			/*}
			catch(ParseException e)
			{
				SimpleDateFormat sdf2 = new SimpleDateFormat("[mm:ss]");
				parsedData =  sdf2.parse(m.group(1));
			}*/
			return parsedData;
		}
		return null;
	}
	

}
