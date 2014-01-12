package Phonetic;

import java.io.File;

public class formateFileName {
	
	public static void formateFileName(File folder)
	{
		File[] filelist = folder.listFiles();
		for(File file : filelist)
		{
			if(!file.renameTo(new File(file.getParent()+"/"+file.getName().replace("IN'", "ING"))))
				System.out.println(file.getName()+": Rename fails.");
			/*if(file.getName().contains(".lrc"))
				file.renameTo(new File(file.getParent()+"/"+file.getName().replace(".lrc", "").trim()+".lrc"));*/
		}
	}
	
	public static void checkFileName(File folder)
	{
		File[] filelist = folder.listFiles();
		int count = 0;
		for(File file : filelist)
		{
			boolean same = true;
			if(!new File(file.getParent()+"/"+file.getName().replace(".lrc", ".mid")).exists())
			{
				System.out.println(file.getName()+": Name is different.");
				count ++;
				same = false;
			}
			else if(!new File(file.getParent()+"/"+file.getName().replace(".mid", ".lrc")).exists())
			{
				System.out.println(file.getName()+": Name is different.");
				count ++;
				same = false;
			}
			/*if(file.getName().contains("-") && same)
				System.out.println(file.getName()+": Name is different.");*/
			/*if(file.getName().contains("("))
				System.out.println(file.getName());*/
		}
		System.out.println(count);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/Users/apple/Downloads/song/lrc_midi");
		//formateFileName(file);
		checkFileName(file);
		//System.out.print(file.getParent());
		
	}

}
