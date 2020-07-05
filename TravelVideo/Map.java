/*
 * Map.java
 * @author Carlos Daniel Avila Navarro
 * 20/03/2020
 */
package TravelVideo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Map 
{
	private boolean isFirst = true; //Needed to decide which map to make start-end or all locations
	private static String key = null; //Key needed to access the API
	
	/*
	 * @function creates a Map object with the key given
	 */
	public Map(String k)
	{
		key = k;
	}
	
	/*
	 * @param pos is the array with all the latitudes, longitudes from the images/videos
	 * @function downloads the map from the API
	 */
	private void getMap(Multimedia[] data)
	{
		String size = "1000,1000@2x"; //The @2x defines that the image has a retina size
		String name = "Map.jpg";
		try 
		{
			String url = "https://www.mapquestapi.com/staticmap/v5/map?" //URL of the API
			+ "&declutter=true" //Enables declutter, it separates near markers
			+ "&key=" + key //Key needed to access the API
			+ "&size=" + size //Size of the image
			+ "&locations=" + data[0].position + "|flag-start"; //Sets the start flag
			if(!isFirst)
			{
				for(int x=1; x<data.length-1; x++)
				{
					url += "||" + data[x].position + "|marker-sm"; //Sets markers except start and end as black
				}
				name = "Map2.jpg";
			}
			url += "||" + data[data.length-1].position + "|flag-end"; //Sets the end flag
			URL mapURL = new URL(url);
			BufferedImage map = ImageIO.read(mapURL); //Gets the image from the URL
			ImageIO.write(map, "jpg", new File(name));
			isFirst = false;
		}
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
	}
	
	/*
	 * @param pos is the array with all the latitudes, longitudes from the images/videos
	 * @function creates both maps, one with start-end and other with all the location
	 */
	public void createMaps(Multimedia[] data)
	{
		getMap(data);
		getMap(data);
	}
}