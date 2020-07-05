/*
 * TravelVideo.java
 * @author Carlos Daniel Avila Navarro
 * 12/03/2020
 */
package TravelVideo;

import java.io.File;
import java.util.Arrays;

public class TravelVideo 
{
	static String key = "I6XOkDDgFT4CChZ8Kp2rkIfFSzkC891M";
	public static int repeated = 0;
	public static void main(String[] args)
	{
		String directory = "Images\\"; //Directory with all images/videos
		File dir = new File(directory);
		String[] files = dir.list();
		Multimedia[] data = new Multimedia[files.length];
		for(int x=0; x<files.length; x++)
		{
			data[x] = new Multimedia(directory, files[x]);
		}
		Arrays.sort(data);
		Map map = new Map(key);
		map.createMaps(data);
		Utilities.createVideo(data);
		System.out.println("The video has been created, its name is output.mp4");
	}
}