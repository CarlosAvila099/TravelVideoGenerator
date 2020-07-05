/*
 * Exiftool.java
 * @author Carlos Daniel Avila Navarro
 * 24/03/2020
 */
package TravelVideo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exiftool extends Commands
{
	private String environment = null; //Environment in which the commands will be run
	
	/*
	 * @function creates an Exiftool object that has the environment given by Commands. Adds exiftool.exe to run exiftool commands
	 */
	public Exiftool()
	{
		environment = Commands.environment + "exiftool.exe ";
	}
	
	/*
	 * @param path is the directory where the file is found
	 * @param name is the name of the image or video
	 * @return returns a String[] with all information acquired from the command exiftool.exe
	 */
	public String[] exif(String name)
	{
    	String[] exif = {""};
        String reader = null;
		String allLines = "";
		try
		{
			Process p = Runtime.getRuntime().exec(environment + name); //Run command with the environment of the instance
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        while ((reader = stdInput.readLine()) != null) //Reads every line of information from the exiftool.exe 
	        {
	        	allLines += reader + "\n"; //Adds a character that doesn't appear in the information, to separate it into an array
	        }
	        exif = allLines.split("\n"); //Gets the information separated in an array
		}
		catch(IOException e) 
		{ 
			System.out.println("The command " + environment + name + " has failed, this is the cause: ");
			e.printStackTrace(); 
		}
		return exif;
	}
	
	/*
	 * @param name is the name of the file we are going to get its rotation
	 * @return String with rotation information
	 */
	public String getRotation(String name)
	{
		String command = "-Orientation " + name; //Command to get orientation of a file
		String rotation = "";
		String line = null;
		try
		{
			Process p = Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = stdInput.readLine()) != null)
			{
				String[] temp = line.split(": ");
				if(temp[0].split(" ")[0].equals("Orientation")) //Finds line that has orientation information 
				{
					rotation = temp[temp.length-1]; //Gets rotation
					break;
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		return rotation;
	}
}