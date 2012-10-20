package mad.widget.connections;

import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Klasa uzywana na urzadzeniach podlaczonych do Internetu w celu wydobycia
 * informacji ze strony Wydzialu Informatyki ZUT o parzystosci dnia obecnego i
 * nastepnego zgodnie z planem zajêc.
 * 
 * @author Grzegorz Fabisiak, Dawid Glinski
 * 
 */
public class WeekParityChecker {

	/** Zmienna zawierajaca adres strony. */
	private static String ZUT_WI_URL = "http://wi.zut.edu.pl";

	/** Obiekt klasy HttpConnect, sluzacy do polaczenia ze strona */
	private static HttpConnect strona = null;

	/**
	 * Zmienna zawierajaca adres strony z danymi o parzystosci tygodnia w
	 * formacie JSON
	 */
	private static String ZUT_WI_JSON = "http://wi.zut.edu.pl/components/com_kalendarztygodni/zapis.json";

	/**
	 * Zmienna pomocna dla programistow w celu ustalenia dzialania klasy
	 * (debugging).
	 */
	private static final String TAG = "WeekParityChecker";

	/** Domyslny konstruktor klasy. */
	public WeekParityChecker() {
	}

	/**
	 * Metoda zwraca tablice stringow, ktora mowi czy dzien obecny i nastêpny
	 * jest nieparzysty/parzysty.
	 * 
	 * @return Tablica stringow mowiaca o nieparzystosci/parzystosci dnia
	 *         obecnego i nastêpnego.
	 */
	public String[] getParity() {
		String pageSource = WeekParityChecker.getURLSource(ZUT_WI_JSON);

		String[] currentWeek = new String[2];

		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		c.add(Calendar.DAY_OF_YEAR, 1);
		int dayNext = c.get(Calendar.DAY_OF_MONTH);

		String today = "_" + Integer.toString(year) + "_"
				+ Integer.toString(month) + "_" + Integer.toString(day);
		String tomorrow = "_" + Integer.toString(year) + "_"
				+ Integer.toString(month) + "_" + Integer.toString(dayNext);

		Log.d(TAG, today + " " + tomorrow);

		try {
			JSONObject pageSrcObject = new JSONObject(pageSource);

			if (pageSrcObject.has(today)) {
				currentWeek[0] = pageSrcObject.getString(today);
			} else
				currentWeek[0] = "?";

			if (pageSrcObject.has(tomorrow))
				currentWeek[1] = pageSrcObject.getString(tomorrow);
			else
				currentWeek[1] = "?";

		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 2; i++) {
			if (currentWeek[i].equals("x"))
				currentWeek[i] = "---";
			else if (currentWeek[i].equals("p"))
				currentWeek[i] = "parzysty";
			else if (currentWeek[i].equals("n"))
				currentWeek[i] = "nieparzysty";
			else
				currentWeek[i] = "?";
		}

		return currentWeek;
	}

	/**
	 * Metoda zwraca HashMap ze wszystkimi informacjami o
	 * nieparzystosci/parzystosci danego dnia tygodnia
	 * 
	 * @return HashMap z informacjami o wszystkich dniach (ich
	 *         nieparzystosci/parzystosci)
	 */
	public HashMap<String, String> getAllParity() {

		String pageSource = WeekParityChecker.getURLSource(ZUT_WI_JSON);
		HashMap<String, String> daysParityMap = new HashMap<String, String>();

		try {
			JSONObject pageSrcObject = new JSONObject(pageSource);
			JSONArray dates = pageSrcObject.names();

			for (int i = 0; i < dates.length(); i++) {
				if (pageSrcObject.has(dates.get(i).toString())) {

					String date = dates.get(i).toString();
					String weekType = pageSrcObject.getString(date);

					daysParityMap.put(date, weekType);
					Log.d(TAG + " date" + i + 1, date);
					Log.d(TAG + " week" + i + 1, weekType);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return daysParityMap;
	}

	/**
	 * Metoda zwraca zrodlo strony jako String.
	 * 
	 * @param url
	 *            zmienna zawierajaca adres strony do pobrania.
	 * @return zrodlo strony jako zmienna typu String.
	 */
	private static String getURLSource(String url) {
		String do_obrobki = "";

		strona = new HttpConnect(10000, url);
		do_obrobki = strona.getPage();

		return do_obrobki;
	}

	/**
	 * Metoda wyszukuje i wycina podciag zawarty pomiêdzy znacznikami
	 * 
	 * @param searching_string
	 *            tekst, w ktorym szuka siê zadanego podciagu
	 * @return znaleziony podciag
	 */
	private CharSequence Parse(String searching_string) {
		int begin_extract = searching_string.indexOf("<div id=\"dzien\">") + 16;
		int end_extract = searching_string.indexOf("<div id=\"pathway\">",
				begin_extract);

		if (begin_extract < 0 || end_extract < 0)
			return null;

		CharSequence div = searching_string.subSequence(begin_extract,
				end_extract);

		return div;
	}

}
