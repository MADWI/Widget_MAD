package com.mad.widget.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * Klasa odpowiedzialan za pobieranie listy grup ze strony oraz pobieranie planu
 * zajec dla kazdej grupy
 * 
 * @author Sebastian Peryt
 * 
 */
public class WidgetDownload extends Activity { // nie wiedzialem jak to obejsc,
	// zeby wykorzystac getResources

	private static final String TAG = "MAD WIZUT Widget";
	String urlStrony;
	final static String siteIn = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/";

	/**
	 * Funkcja na podstawie zadanego ciagu wejsciowego zwraca tablice z numerami
	 * grup.
	 * 
	 * @param String
	 *            adres strony z ktorej ma byc pobrany plan (w wersji finalnej
	 *            bedzie usuniety)
	 * @param String
	 *            numer grupy np. I1 do wyszukania
	 * @return Tablica stringow ze znalezionymi grupami
	 * @author Sebastian Peryt
	 */
	public String[] getGroups(String rodzajStudiow, String kierunek,
			int stopien, int rok) {
		Log.d(TAG, "Stopien: " + new Integer(stopien).toString());
		Log.d(TAG, "Rok: " + new Integer(rok).toString());

		HttpConnect con = new HttpConnect(10000, siteIn + rodzajStudiow);
		String site = null;
		// String[] outputTab = new String[]; //- Pamiec jest chyba dynamicznie
		// przydzielana to co jest nie tak?
		try {
			site = con.getStrona();
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
			String[] outputTab = new String[1];
			outputTab[0] = "B³ednie podane dane";
			Log.d(TAG, outputTab[0]);
			return outputTab;
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
	 *            String Rodzaj studiów z du¿ej litery
	 * @param kierunek
	 *            String Kierunek studiów z du¿ej litery
	 * @param stopien
	 *            int Stopien studów
	 * @param rok
	 *            int Rok studiow
	 * @return pattern jeœli dane istnieje, null jesli nie.
	 * @author Sebastian Peryt
	 */
	private Pattern getRodzaj(String rodzaj, String kierunek, int stopien,
			int rok) {
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

	/**
	 * Funkcja sprawdza czy folder do przechowywania planu istnieje i ew. go
	 * tworzy
	 * 
	 * @return true jesli folder istniej lub utworzono, false jesli wystapil
	 *         blad
	 * @author Sebastian Peryt
	 */
	private boolean setFolder() {
		String newFolder = "/MAD_Plan_ZUT"; // nie wiem czemu nie dziala
		// this.getString(R.string.folder_name);
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
	 * <b>TODO</b>Sprawdzic co sie dzieje jesli nie ma takiego planu na
	 * serwerze, lub nie ma internetu
	 * 
	 * @param String
	 *            forma studiow - Stacjonarne, Niestacjonarne (Musi byc z
	 *            wielkiej litery)
	 * @param String
	 *            Pelny numer grupy dla korego ma zostac pobrany plan np. I1-22
	 * @return true jesli pomyslnie pobrano plan
	 * @author Sebastian Peryt
	 */
	private boolean downloadPlan(String forma, String grupa) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		/**
		 * Najprawdopodobniej jest to zbêdny fragment kodu, ale do momentu
		 * zakonczenia testów, lepiej zostawic :)
		 */
		try {
			WidgetDownload.this.setFolder();
			Log.d(TAG, "setFolder - ok");
		} catch (Exception e) {
			Log.e(TAG, "setFolder - Blad " + e);
		}
		if (WidgetDownload.this.setFolder()) {
			Log.d(TAG, "Folder utworzony");
		} else {
			Log.e(TAG, "B³ad utworzenia folderu");
		}

		try {
			URL url = new URL(siteIn + forma + "/" + grupa + ".pdf");
			File file = new File(extStorageDirectory + "/MAD_Plan_ZUT/" + grupa
					+ ".pdf");

			// long startTime = System.currentTimeMillis();// Poczatek
			// pobierania
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d(TAG, "downloaded");// koniec pobierania
			return true;

		} catch (IOException e) {
			Log.d(TAG, "Last Error: " + e);
			return false;
		}
	}

	/**
	 * <b>TODO</b> Funkcja wyrzuca NullPointerException, mo¿liwe, ¿e problem
	 * jest gdzies przy wywolaniu startActivity. <br>
	 * <br>
	 * Funkcja wywo³uje intent, który otwiera plan w pliku pdf
	 * 
	 * @param grupa
	 *            String z numerem grupy
	 * @author Sebastian Peryt
	 */
	private void planExists(String grupa) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + "/MAD_Plan_ZUT/" + grupa
				+ ".pdf");
		if (file.exists()) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			try {
				startActivity(intent);
				/**
				 * W tym miejscu wyrzuca NullPointerException
				 */
			} catch (ActivityNotFoundException e) {
				/*
				 * Toast.makeText(WidgetDownload.this.getApplicationContext(),
				 * "No Application Available to View PDF",
				 * Toast.LENGTH_SHORT).show();
				 */
				Log.d(TAG, "Nie ma czym otworzyc PDF");
			}
		}
	}

	/**
	 * Funkcja sprawdza, czy plan dla podanej grupy jest pobrany
	 * 
	 * @param grupa
	 * @return true jeœli wszystko jest ok, false w przeciwnym wypadku
	 * @author Sebastian Peryt
	 */
	public boolean openPlan(String forma, String grupa) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + "/MAD_Plan_ZUT/" + grupa
				+ ".pdf");
		if (file.exists()) {
			planExists(grupa);
			return true;
		} else {
			if (downloadPlan(forma, grupa)) {
				planExists(grupa);
				return true;
			} else {
				Log.e(TAG, "Blad pobrania planu");
				return false;
			}
		}
	}
}