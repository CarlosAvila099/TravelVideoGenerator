/*
 * Cleaner.java
 * @author Carlos Daniel Avila Navarro
 * 24/03/2020
 */
package TravelVideo;

import java.io.File;

public class Cleaner 
{	
	/*
	 * @function eliminates the temporal files
	 */
	public static void clearTemp()
	{
		String[] names = {"silence.ac3", "output2.mp4", "Map.jpg", "Map2.jpg"};
		for(int x=0; x<names.length; x++)
		{
			Cleaner.deleteFile(names[x]);
		}
	}
	
	/*
	 * @param name is the name of the file to be eliminated
	 * @function deletes the file given
	 */
	public static void deleteFile(String name)
	{
		File file = new File(name);
		if(file.exists()) { file.delete(); }
	}
}
