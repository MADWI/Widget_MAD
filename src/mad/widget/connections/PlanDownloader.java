package mad.widget.connections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mad.widget.utils.Constans;
//import mad.widget.utils.SharedPrefUtils;

import android.content.Context;
//import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

/**
 * Klasa odpowiedzialan za pobieranie listy grup ze strony oraz pobieranie planu
 * zajec dla kazdej grupy
 * 
 * @author Sebastian Peryt, SŒ
 * 
 */
public class PlanDownloader {

	private static final String TAG = "WidgetDownload";
	private static final String siteIn = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/";

	/**
	 * Funkcja na podstawie zadanego ciagu wejsciowego zwraca tablice z numerami
	 * grup.
	 * 
	 * @param String
	 *            numer grupy np. I1 do wyszukania
	 * @return Tablica stringow ze znalezionymi grupami
	 * @author Sebastian Peryt
	 */
	public static String[] getGroups(String rodzajStudiow, String kierunek,
			int stopien, int rok) {
		Log.d(TAG, "Stopien: " + Integer.valueOf(stopien));
		Log.d(TAG, "Rok: " + Integer.valueOf(rok));

		HttpConnect con = new HttpConnect(10000, siteIn + rodzajStudiow);
		String site = null;

		// String[] outputTab = new String[]; //- Pamiec jest chyba dynamicznie
		// przydzielana to co jest nie tak?
		try {
			site = con.getPage();
			Log.d(TAG, "Polaczono ze strona");
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		if ("" == site) {
			Log.e(TAG, "Error con.getStrona()");
		}

		// wybor kierunku i roku
		Pattern p = null;
		p = getRodzaj(rodzajStudiow, kierunek, stopien, rok);

		// sP = this.getRodzaj(rodzajStudiow, kierunek, stopien, rok);

		/**
		 * W momencie gdy zle zostana podane dane: rok; kierunek; rodzaj;
		 */
		if (null == p) {
			Log.d(TAG, "b³edne dane, zwracam nulla...");
			return null;
		}

		// p = Pattern.compile(sP);

		Matcher m = p.matcher(site);
		int i = 0;
		while (m.find()) {
			i++;
		}
		m.reset();
		String[] outputTab = new String[i];
		i = 0;
		while (m.find()) {
			outputTab[i] = m.group().subSequence(1, m.group().indexOf(".pdf"))
					.toString();
			Log.d(TAG, outputTab[i]);
			i++;
		}
		return outputTab;
	}

	/**
	 * 
	 * @param rodzaj
	 *            String Rodzaj studiï¿½w z duï¿½ej litery
	 * @param kierunek
	 *            String Kierunek studiï¿½w z duï¿½ej litery
	 * @param stopien
	 *            int Stopien studï¿½w
	 * @param rok
	 *            int Rok studiow
	 * @return pattern jeï¿½li dane istnieje, null jesli nie.
	 * @author Sebastian Peryt
	 */
	private static Pattern getRodzaj(String rodzaj, String kierunek,
			int stopien, int rok) {
		Pattern p = null;
		if (rodzaj.equals("Stacjonarne")) {
			if (kierunek.equals("Bioinformatyka")) {
				p = Pattern.compile(">BI" + stopien + "-" + rok
						+ "[0-9]{1,2}\\.pdf<");
			} else if (kierunek.equals("Informatyka")) {
				p = Pattern.compile(">I" + stopien + "-" + rok
						+ "[0-9]{1,2}\\.pdf<");
			} else if (kierunek.equals("ZIP")) {
				p = Pattern.compile(">ZIP" + stopien + "-" + rok
						+ "[0-9]{1,2}\\.pdf<");
			}
			return p;
		} else if (rodzaj.equals("Niestacjonarne")) {
			if (kierunek.equals("Bioinformatyka")) {
				p = null;
			} else if (kierunek.equals("Informatyka")) {
				p = Pattern.compile(">I" + stopien + "n-" + rok
						+ "[0-9]{1,2}\\.pdf<");
			} else if (kierunek.equals("ZIP")) {
				p = Pattern.compile(">ZIP" + stopien + "n-" + rok
						+ "[0-9]{1,2}\\.pdf<");
				/*
				 * if(1==rok) { p =
				 * Pattern.compile(">ZIP"+stopien+"n-"+rok+"[0-9]{1,2}\\.pdf<");
				 * } else { p = null; }
				 */
			}
			return p;
		}

		return null;
	}

	// /**
	// * Funkcja sprawdza czy folder do przechowywania planu istnieje i ew. go
	// * tworzy
	// *
	// * @return true jesli folder istniej lub utworzono, false jesli wystapil
	// * blad
	// * @author Sebastian Peryt
	// */
	private static boolean setFolder() {
		String newFolder = Constans.PLAN_FOLDER;
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File myNewFolder = new File(extStorageDirectory + newFolder);
		if (!myNewFolder.exists())// folder nie istnieje
		{
			if (myNewFolder.mkdir()) {
				return true;
			} else {
				return false;
			}
		}
		return true;// folder istnieje
	}

	/**
	 * Funkcja do pobierania planu.
	 * 
	 * @param String
	 *            forma studiow - Stacjonarne, Niestacjonarne (Musi byc z
	 *            wielkiej litery)
	 * @param String
	 *            Pelny numer grupy dla korego ma zostac pobrany plan np. I1-22
	 * @return true jesli pomyslnie pobrano plan
	 * @author Sebastian Peryt
	 */
	public static boolean downloadPlan(Context ctx, String forma, String grupa) {

		if (setFolder()) {

			try {
				URL url = new URL(siteIn + forma + "/" + grupa + ".pdf");

				// create the new connection
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				// set up some things on the connection
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);

				// and connect!
				urlConnection.connect();

				// set the path where we want to save the file
				// in this case, going to save it on the root directory of the
				// sd card.
				File SDCardRoot = Environment.getExternalStorageDirectory();
				// create a new file, specifying the path, and the filename
				// which we want to save the file as.

				File file = new File(SDCardRoot + Constans.PLAN_FOLDER + "/"
						+ grupa + ".pdf");

				// this will be used to write the downloaded data into the file
				// we
				// created
				FileOutputStream fileOutput = new FileOutputStream(file);

				// this will be used in reading the data from the internet
				InputStream inputStream = urlConnection.getInputStream();

				// create a buffer...
				byte[] buffer = new byte[1024];
				int bufferLength = 0; // used to store a temporary size of the
										// buffer

				// now, read through the input buffer and write the contents to
				// the
				// file
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					// add the data in the buffer to the file in the file output
					// stream (the file on the sd card
					fileOutput.write(buffer, 0, bufferLength);

				}
				// close the output stream when done
				fileOutput.close();

				// catch some possible errors...
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			Log.e(TAG, "Nie mozna utworzyc folderu");
			return false;
		}

	}

	/**
	 * 
	 * Funkcja sprawdza czy plan ju¿ istenieje na dysku
	 * 
	 * @param grupa
	 *            String z numerem grupy
	 * @author Sebastian Peryt
	 */
	public static boolean planExistsAndNew(Context ctx, String forma,
			String grupa) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + Constans.PLAN_FOLDER + "/"
				+ grupa + ".pdf");
		if (file.exists()) {
			return true;
		} else
			return false;
	}

	public static boolean removePlans() {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ Constans.PLAN_FOLDER);

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				new File(dir, children[i]).delete();
			}
			return true;
		} else
			return false;
	}

	public static void removePlan(String path) {

		new File(path).delete();

	}

}