package com.mad.widget;

import java.net.*;
//import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class can be applied on an Internet-connected device to extract 
 * information on the parity of the current week according to the schedule 
 * of West Pomeranian University of Technology in Szczecin, Poland.
 * It retrives the required information from the said university's website
 * but it is not limited to it, i.e. the URL can be modified, etc.
* @author      bocianowsky
*/
public class WeekParityChecker
{
	private static String ZUT_WI_URL;
	private static String TAG_NAME;
	private static String TAG_ID;
	private static String oddMarker = "nieparzysty", oddMarkerMessage = "nieparzysty";
	@SuppressWarnings("unused")
	private static String evenMarker = "parzysty", evenMarkerMessage = "parzysty";
	
	public WeekParityChecker()
	{
		ZUT_WI_URL = "http://wi.zut.edu.pl";
		TAG_NAME = "div";
		TAG_ID = "dzien";
	}
	public WeekParityChecker(String page_url, String tag_name, String tag_id)
	{
		ZUT_WI_URL = page_url;
		TAG_NAME = tag_name;
		TAG_ID = tag_id;
	}
	
	public static void main(String args[])
	{
		WeekParityChecker checker = new WeekParityChecker();
		String currentWeekStatus = checker.getParity(); 
		System.out.printf("ZUT-owski tydzieñ jest %s.", currentWeekStatus);
	}
	
	/**
	 * Returns a boolean value, true if the week is odd, false if it is even.
	 * 
	 * @return		the parity of the week
	 */
	public boolean isOdd()
	{
		String pageSource = WeekParityChecker.getURLSource(ZUT_WI_URL);
		if (pageSource == null) //an error has occurred
		{
			System.out.println("Error while retriving source page!!!");
			//Log.e(TAG, "Received an exception while getting the source of the webpage.", exception);
		}
		String extractedTag = this.extractTag(pageSource);
		boolean result;
		if (true == this.hasSubstring(extractedTag, oddMarker))
			result = true;
		else // (true == this.hasSubstring(extractedTag, evenMarker))
			result = false;
		return result; // the week is: 1 - odd, 0 - even
	}
	/**
	 * Returns the string which tells whether the week is odd or even
	 * 
	 * @return 		string which signifies whether the week is odd or even
	 */
	public String getParity()
	{
		return (this.isOdd() ? oddMarkerMessage : evenMarkerMessage);
	}
	
	private boolean hasSubstring(String inString, String subString)
	{
		int index = inString.indexOf(subString);
		if (index == -1)
			return false;
		else
			return true;
	}

	private static String getURLSource(String url)
    {
		final char newline = '\n';
        try
        {
            URL pageURL = new URL(url);
            StringBuilder sourceText = new StringBuilder();
            Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");
            try
            {
                while (scanner.hasNextLine()){
                    sourceText.append(scanner.nextLine() + newline);
                }
            }
            finally
            {
                scanner.close();
            }
            return sourceText.toString();
        }
        /* Error handling MUST BE improved soon! */
        catch (MalformedURLException ex)
        {
            // invalid URL, this should be handled by us, don't you think?
        } 
        //catch (IOException ex) {
        //    // connection not established
        //    return null;
        //}
        catch(Exception ex) 
        {
        	// other unspecified error
            return null;
        }
		return null;
    }
	
	private String extractTag(String htmlString)
	{
		//String regexTag = "<"+TAG_NAME+"\\s*id=\""+TAG_ID+"\\s*>([^*<]+)</"+TAG_NAME+">"; //doesn't work..
		String regexTag = "<"+TAG_NAME+" [^>]*id=\""+TAG_ID+"\"[^>]*>(.*?)</div>"; //this one does!!! uff..
	    Pattern pattern = Pattern.compile(regexTag, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher matcher = pattern.matcher(htmlString);
	    String fullTag = "";
	    boolean patternFound = false;
	    while (matcher.find())
	    {
	    	patternFound = true;
	    	fullTag = matcher.group();
	    }
	    if (patternFound == false)
	    {
	    	return null;
	    } 
	    else 
	    {
	    	return fullTag;
	    }
	}
}
