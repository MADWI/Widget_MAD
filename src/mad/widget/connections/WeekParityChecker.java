package mad.widget.connections;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * This class can be applied on an Internet-connected device to extract
 * information on the parity of the current week according to the schedule of
 * West Pomeranian University of Technology in Szczecin, Poland. It retrives the
 * required information from the said university's website but it is not limited
 * to it, i.e. the URL can be modified, etc.
 * 
 * @author bocianowsky, Dawid
 * 
 */

public class WeekParityChecker {

    private static String ZUT_WI_URL = "http://wi.zut.edu.pl";

    private static final String TAG = "WeekParityChecker";

    public WeekParityChecker() {
    }

    /**
     * Returns the string which tells whether the week is odd or even
     * 
     * @return string which signifies whether the week is odd or even
     */
    public String[] getParity() {

	String pageSource = WeekParityChecker.getURLSource(ZUT_WI_URL);

	if (pageSource.equals("") || pageSource.equals(null))
	    return null;
	String extracted = Parse(pageSource).toString();

	int begin_extract = extracted.indexOf("title=\"") + 7;
	int end_extract = extracted.indexOf("\"", begin_extract);

	if (begin_extract < 0 || end_extract < 0)
		return null;

	System.out.println("Poczatek znacznika1: " + begin_extract);
	System.out.println("Koniec znacznika1: " + end_extract);

	String[] currentWeek = new String[2];
	currentWeek[0]= (String) extracted.subSequence(begin_extract,
			end_extract);
	
	int begin_extract_next = extracted.indexOf("title=\"",end_extract) + 7;
	int end_extract_next = extracted.indexOf("\"", begin_extract_next);
	
	System.out.println("Poczatek znacznika2: " + begin_extract_next);
	System.out.println("Koniec znacznika2: " + end_extract_next);
	
	if (begin_extract_next < 0 || end_extract_next < 0)
		return null;
	
	currentWeek[1]= (String) extracted.subSequence(begin_extract_next,
		end_extract_next);
	
	Log.d(TAG,currentWeek[0] +" "+ currentWeek[1]);
	return currentWeek;

    }

    /**
     * This method returns page source code as a string
     * 
     * @param string
     *            which contain URL address
     * @return page source code as a string
     * @author Dawid
     */

    private static String getURLSource(String url) {
	String do_obrobki = "";

	HttpConnect strona = new HttpConnect(10000, url);
	do_obrobki = strona.getPage();

	return do_obrobki;
    }

    /**
     * This method is searching substring which is on the '<>' and '</>' symbols
     * 
     * @param where
     *            method must seek a substring
     * @return found text
     * @author Dawid
     */
    private CharSequence Parse(String searching_string) {
	int begin_extract = searching_string.indexOf("<div id=\"dzien\">") + 16;
	int end_extract = searching_string.indexOf("<div id=\"pathway\">",
		begin_extract);

	if (begin_extract < 0 || end_extract < 0)
	    return null;

	CharSequence div = searching_string.subSequence(begin_extract,
		end_extract);
	
	//string to test
	//div= "<div id=\"weekcal\" style=\"cursor:default;\"><span style=\"color:red\" title=\"parzysty\">2012:10:11</span> &bull; <span style=\"color:red\" title=\"nieparzysty\">2012:10:12</span> &bull";

	return div;
    }

}
