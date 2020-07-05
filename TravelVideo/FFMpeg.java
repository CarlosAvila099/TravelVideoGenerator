/*
 * FFMpeg.java
 * @author Carlos Daniel Avila Navarro
 * 24/03/2020
 */
package TravelVideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFMpeg extends Commands
{
	private String environment = null; //Environment in which the commands will be run
	
	/*
	 * @function creates an FFMpeg object that has the environment given by Commands. Adds ffmpeg to run FFMpeg commands
	 */
	public FFMpeg()
	{
		environment = Commands.environment + "ffmpeg ";
	}
	
	/*
	 * @return a boolean with true if the silence.ac3 file was created successfully
	 */
	public boolean createSilence()
	{
		boolean success = false;
		Cleaner.deleteFile("silence.ac3");
		String command = "-f lavfi -i anullsrc=channel_layout=5.1:sample_rate=48000 -t 1 silence.ac3"; //Command to create an audio file with silence
		try
		{
			Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
	        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg commands work
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		File file = new File("silence.ac3"); //Different File variable needs to be created so it works properly 
		success = file.exists();
		return success;
	}
	
	/*
	 * @param name is the name of the file we are going to add silence.ac3
	 * @function adds the audio file silence.ac3 to the file given
	 */
	public void addSilence(String name)
	{
		Windows windows = new Windows();
		Cleaner.deleteFile("tempSilence.mp4");
		String command = "-i " + name + " -i silence.ac3 -c copy -map 0:v:0 -map 1:a:0 tempSilence.mp4"; //Command to add audio file to video file
		try
		{
			Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg command work
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
        windows.rename("tempSilence.mp4", name);
	}
	
	/*
	 * @param path is the directory of the images
	 * @param imagesNames is the array that has the names of the images to convert into a video
	 * @param name is the name of the video
	 * @return a boolean with true value if the video was created
	 */
	public boolean imagesToVideo(Multimedia[] data, String name)
	{
		Cleaner.deleteFile(name + ".mp4");
		boolean success = false;
		String command = "type"; //Command to join images into a video
		for(int x=0; x<data.length; x++)
		{
			if(!data[x].name.equals("Map.jpg") && !data[x].name.equals("Map2.jpg")) //Checks if the image has Orientation
			{
				data[x].name = rotateImage(data[x].name, data[x].orientation); //If needed, changes orientation without modifying original image
			}
			command += " " + data[x].name; //Concatenating the file names into the command
		}
		command += " | ffmpeg -f image2pipe -framerate .5 -i - -c:v libx264 -vf format=yuv420p,setsar=1:1,scale=1000:1000 -r 25 " + name + ".mp4"; //Completing command
		try
		{
			Process p = Runtime.getRuntime().exec("cmd /c " + command); //Run command with CMD as environment
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));            
			while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg commands work
		}
		catch(IOException e)
		{
			System.out.println("The command " + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		File file = new File(name + ".mp4");
		success = file.exists();
		addSilence(name + ".mp4");
		for(int x = 0; x< TravelVideo.repeated; x++)
		{
			Cleaner.deleteFile("tempRotation" + x + ".jpg");
		}
		TravelVideo.repeated = 0;
		return success;
	}

	/*
	 * @name is the video file with its path
	 * @return a boolean with true if the process was made
	 */
	public boolean configureVideo(String name)
	{
		Windows windows = new Windows();
		boolean success = false;
		Cleaner.deleteFile("tempVideo.jpg");
		String command = "-i " + name + " -vcodec libx264 -vf format=yuv420p,scale=1000:1000 tempVideo.mp4"; //Changes the video given to have the same characteristics as the other ones
		try
		{
			Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg command work
			command = "-i tempVideo.mp4 -vf setsar=1:1 tempVideo2.mp4"; //Needed to join later
			p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
			stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg commands work
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		windows.rename("tempVideo2.mp4", "tempVideo.mp4");
		File file = new File("tempVideo.mp4");
		success = file.exists();
		return success;
	}

	/*
	 * @param name is the name of the file we are going to rotate, if necessary
	 * @param rotation is the rotation found by getRotation()
	 * @return String with the new name of the rotated image, needed so it doesn't modifies the original image
	 */
	public String rotateImage(String name, String rotation)
	{
		String newName = name;
		if(!rotation.equals("Horizontal (normal)")) //Normal rotation
		{
			String command  = "-i " + name + " -vf \"transpose="; //Command to change rotation with FFMpeg
			boolean clock = rotation.split(" ")[2].equals("CW"); //Checks if rotation is clockwise or counter-clockwise
			if(clock) { command += "1\" tempRotation" + TravelVideo.repeated + ".jpg"; } //Changes clockwise rotation to normal
			else { command += "2\" tempRotation" + TravelVideo.repeated + ".jpg"; } //Changes counter-clockwise rotation to normal
			newName = "tempRotation" + TravelVideo.repeated + ".jpg";
			TravelVideo.repeated++;
			try
			{
				Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while((stdError.readLine()) != null) {  } //Needed so that FFMpeg commands work
			}
			catch(IOException e)
			{
				System.out.println("The command " + environment + command + " has failed, this is the cause: ");
				e.printStackTrace();
			}
		}
		return newName;
	}	
	
	/*
	 * @param first is the name of the file all will be joined into
	 * @param second is the video to join with the first
	 * @return a boolean with true value if the join operation was made
	 */
	public boolean join(String first, String second)
	{
		Windows windows = new Windows();
		boolean success = false;
		Cleaner.deleteFile("tempJoin.mp4");
		String command = "-vsync 2 -i " + first + " -i " + second + " -filter_complex \"[0:0] [0:1] [1:0] [1:1]"
				+ " concat=n=2:v=1:a=1: [v] [a]\" -map \"[v]\" -map \"[a]\" tempJoin.mp4"; //Concatenate both video and audio with complex filters
		try
		{
			Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((stdError.readLine()) != null) {  } //Needed so that FFMpeg commands work
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		success = windows.rename("tempJoin.mp4", first); //Renames the file to the name of first
		return success;
	}
}