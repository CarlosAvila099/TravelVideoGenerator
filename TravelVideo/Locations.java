/*
 * Locations.java
 * @author Carlos Daniel Avila Navarro
 * 20/03/2020
 */
package TravelVideo;

public class Locations
{
	String dmsPosition;
	String decimalPosition;
	
	/*
	 * @param degreePosition
	 * @function creates a Locations object
	 */
	public Locations(String degreePosition)
	{
		dmsPosition = degreePosition;
		decimalPosition = dmsTodecimal(degreePosition);
	}
	
	/*
	 * @param pos is the string containing the latitude and longitude in degrees, minutes and seconds
	 * @return a String of the given latitude and longitude converted to decimal
	 */
	private String dmsTodecimal(String pos)
	{
		String[] dmsLat = pos.split(", ")[0].split(" "); //Gets the latitude as an array separated in degrees, minutes and seconds
		String[] dmsLon = pos.split(", ")[1].split(" "); //Gets the longitude as an array separated in degrees, minutes and seconds
		float decimalLat = Float.parseFloat(dmsLat[0]) + findDecimal(dmsLat);
		float decimalLon = Float.parseFloat(dmsLon[0]) + findDecimal(dmsLon);
		if(dmsLat[dmsLat.length-1].equals("S")) //Checks if the value is positive or negative
		{
			decimalLat = decimalLat * -1; 
		}
		if(dmsLon[dmsLon.length-1].equals("W")) //Checks if the value is positive or negative
		{
			decimalLon = decimalLon * -1; 
		}
		return decimalLat + "," + decimalLon;
	}
	
	/*
	 * @param pos is the separated array with degrees, minutes and seconds
	 * @return a float value of the minutes and seconds converted to decimal
	 */
	private float findDecimal(String[] pos)
	{
		float decimal = Float.parseFloat(pos[2].substring(0, pos[2].length()-1))/60;
		decimal = (Float.parseFloat(pos[1].substring(0, pos[1].length()-1)) + decimal)/60;
		return decimal;
	}
}