package DatabaseManager;

import java.io.IOException;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class DatabaseManager {
	private static RecordManager recman;
	private static HTree hashtable;
	public DatabaseManager() throws IOException
	{
		recman = RecordManagerFactory.createRecordManager("Dictionary");
		long recid = recman.getNamedObject("Dictionary");
		if(recid!=0) {
          	hashtable  = HTree.load(recman, recid);
          }
          else {
          	hashtable = HTree.createInstance(recman);
          	recman.setNamedObject("Dictionary", hashtable.getRecid());
          }
	}
	
	public static HTree getHashtableSingleton() throws IOException
	{
		if(hashtable == null)
		{
			new DatabaseManager();
		}
		return hashtable;
	}
	
	public static void commit() throws IOException
	{
		if(recman != null)
		{
			recman.commit();
		}
	}
	
	public static void commitAndClose() throws IOException
	{
		if(recman != null)
		{
			recman.commit();
			recman.close();
		}
	}
	
	public static void close() throws IOException
	{
		if(recman != null)
		{
			recman.close();
		}
	}
}
