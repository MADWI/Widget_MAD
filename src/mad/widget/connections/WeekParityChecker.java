package mad.widget.connections;

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

	private static final String WeekOdd = "d. nieparzysty";
	private static final String WeekEven = "d. parzysty";
	private static final String WeekUnknow = " - ";

	public WeekParityChecker() {
	}

	/**
	 * Returns the string which tells whether the week is odd or even
	 * 
	 * @return string which signifies whether the week is odd or even
	 */
	public String getParity() {
		int result = this.checkParity(ZUT_WI_URL);
		if (result == 1)
			return WeekOdd;
		else if (result == 2)
			return WeekEven;
		else
			return WeekUnknow;
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
	 * This method returns true if week is odd false if even
	 * 
	 * @param string
	 *            which contain URL address
	 * @return true if week is odd, false if even
	 * @author Dawid
	 * */
	private int checkParity(String source) {
		String pageSource = WeekParityChecker.getURLSource(source);

		if (pageSource.equals("") || pageSource.equals(null))
			return 0;
		String extracted = Parse(pageSource).toString();
		Log.i(TAG, "sprawdzono");

		if (extracted.equals(null))
			return 0;
		if (extracted.contains("nieparzystego")
				|| extracted.contains("nieparzysty"))
			return 1;
		else
			return 2;
	}

	/**
	 * This method is searching substring which is on the '<>' and '</>' symbols
	 * (need to update)(na razie dla tytulu zeby sprawdzic czy dziala)
	 * 
	 * @param where
	 *            method must seek a substring
	 * @return found text
	 * @author Dawid
	 */
	private CharSequence Parse(String searching_string) {
		int begin_extract = searching_string.indexOf("<div id=\"dzien\">") + 16;
		int end_extract = searching_string.indexOf("</div>", begin_extract);

		if (begin_extract < 0 || end_extract < 0)
			return null;

		System.out.println("Poczatek znacznika: " + begin_extract);
		System.out.println("Koniec znacznika: " + end_extract);

		CharSequence title = searching_string.subSequence(begin_extract,
				end_extract);

		return title;
	}

}
