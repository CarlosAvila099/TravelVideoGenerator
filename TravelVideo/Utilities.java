/*
 * Utilities.java
 * @author Carlos Daniel Avila Navarro
 * 12/03/2020
 */
package TravelVideo;

public class Utilities
{
	/*
	 * @param array is the String[][] with all the information gathered by the exiftool.exe command
	 * @param Characteristic is the name of the characteristic in the array
	 * @return a String[] with all the information corresponding to the Characteristic
	 */
	public static String getInfo(String[] array, String Characteristic)
	{
		String info = "";
		for(int x=0; x<array.length; x++)
		{
			if(array[x].split("  ")[0].equals(Characteristic)) //Checks if the line we are in, is the one we are looking for
			{
				info = array[x].split("  ")[array[x].split("  ").length - 1].substring(2); //Gets the piece of information
				if(Characteristic.equals("Create Date"))
				{
					if(info.charAt(0) == ' ') { info = info.substring(1); } //Checks if a space was created
					info = info.split(" ")[0].replace(":", "-") + "T" + info.split(" ")[1];
					//Modifies the information so it can be converted to LocalDateTime
				}
				else if(Characteristic.equals("GPS Position"))
				{
					info = info.replace("deg ", ""); //Eliminates the deg parts of the string
					Locations location = new Locations(info);
					info = location.decimalPosition;
					//Modifies the information so it can be used in the map creation
				}
				else if(Characteristic.equals("MIME Type"))
				{
					info = info.split("/")[0].replace(" ", "");
				}
				break;
			}
		}
		return info;
	}

	/*
	 * @param data is an array that has all the data got from the images/videos
	 * @function creates one video with all the images/videos from the folder, with the maps with markers of places visited
	 */
	public static void createVideo(Multimedia[] data)
	{
		FFMpeg ffmpeg = new FFMpeg();
		ffmpeg.createSilence();
		boolean finished = false; //boolean to see if the end of the name[] was reached
		int current = 0; //The current position in the name[]
		int start = 0; //Position from the name[] we start, works to start from the next file after a mp4 file
		boolean occupied = false; //States if the file name output.mp4 is already occupied
		boolean second = false; //States if the file name output2.mp4 is already occupied
		while(!finished)
		{
			for(int x=start; x<data.length; x++)
			{
				if(data[x].type.equals("video") || x == (data.length - 1)) //Checks if a video file is found or if data[] has ended
				{
					current = x;
					break;
				}
			}
			if(current == (data.length-1)) { finished = true; }
			int extra = 0; //It represents how many extra spaces are needed to create the video 0:None or Map2.jpg || 1:Map.jpg
			Multimedia[] temp = null; //Array that will have the data of the images before a video
			if(start == 0)
			{
				extra = 1;
				temp = new Multimedia[current-start+extra];
				temp[0] = new Multimedia("", "Map.jpg");
			}
			else { temp = new Multimedia[current-start]; }
			for(int x=start; x<current; x++)
			{
				if(extra == 1) { temp[x-start+1] = data[x]; } //Arranges the position +1 in case of Map.jpg
				else { temp[x-start] = data[x]; }
			}
			if(!occupied)
			{
				ffmpeg.imagesToVideo(temp, "output");
				occupied = true;
			}
			else
			{
				ffmpeg.imagesToVideo(temp, "output2");
				second = true;
			}
			if(second) { ffmpeg.join("output.mp4", "output2.mp4"); }
			if(data[current].type.equals("video")) //Joins the created video and the video file in the folder given
			{
				ffmpeg.configureVideo(data[current].name);
				ffmpeg.join("output.mp4", "tempVideo.mp4");
				Cleaner.deleteFile("tempVideo.mp4");
			}
			if(current == data.length-1)
			{
				temp = new Multimedia[1];
				temp[0] = new Multimedia("", "Map2.jpg");
				ffmpeg.imagesToVideo(temp, "output2");
				ffmpeg.join("output.mp4", "output2.mp4");
			}
			start = current+1;
		}
		Cleaner.clearTemp();
	}
}