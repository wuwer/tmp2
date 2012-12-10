package org.spybot.main;

public class Commands {
	
	public static String invoke(String command)
	{
		if (command.equals("getID"))
			return getIMEI();
		else if (command.equals("getLocation"))
			return getLocation();
		else return "NoSuchCommandSpecified";
	}

	public static String getIMEI()
	{
		return "SampleIMEI";
	}
	
	public static String getLocation()
	{
		return "SampleLocation";
	}
}
