/*
 * Windows.java
 * @author Carlos Daniel Avila Navarro
 * 24/03/2020
 */
package TravelVideo;

import java.io.File;
import java.io.IOException;

public class Windows 
{
	private String environment = null; //Environment in which the commands will be run
	
	/*
	 * @function creates a Windows object that has the environment given by Commands
	 */
	public Windows() 
	{ 
		environment = Commands.environment;
	}
	
	/*
	 * @param name is the name of the file
	 * @param newName is the name the file will be renamed into
	 * @return a boolean with true value if the rename process was made
	 */
	public boolean rename(String name, String newName)
	{
		boolean success = false;
		Cleaner.deleteFile(newName);
		String command = "ren " + name + " " + newName; //Command to rename files in windows
		try
		{
			Runtime.getRuntime().exec(environment + command); //Run command with the environment of the instance
		}
		catch(IOException e)
		{
			System.out.println("The command " + environment + command + " has failed, this is the cause: ");
			e.printStackTrace();
		}
		File file = new File(newName); //Different File variable needs to be created so it works properly 
		success = file.exists();
		return success;
	}
}
