/*
 * Multimedia.java
 * @author Carlos Daniel Avila Navarro
 * 24/03/2020
 */
package TravelVideo;

import java.time.LocalDateTime;

public class Multimedia implements Comparable<Multimedia>
{
	String name, orientation, position, type;
	LocalDateTime date;
	
	/*
	 * @param path is the directory where the images are found
	 * @param fileName is the name of the file
	 * @function creates a Multimedia object
	 */
	public Multimedia(String path, String fileName)
	{
		name = path + fileName;
		if(!path.equals(""))
		{
			Exiftool exiftool = new Exiftool();
			String[] exif = exiftool.exif(name);
			date = LocalDateTime.parse(Utilities.getInfo(exif, "Create Date"));
			orientation = exiftool.getRotation(name);
			position = Utilities.getInfo(exif, "GPS Position");
			type = Utilities.getInfo(exif, "MIME Type");
		}
	}
	
	@Override
	public int compareTo(Multimedia m)
	{
		return date.compareTo(m.date);
	}
}
