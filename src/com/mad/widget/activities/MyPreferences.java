package com.mad.widget.activities;

import com.mad.widget.R;
import com.mad.widget.http.WidgetDownload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

//import android.widget.SlidingDrawer;

public class MyPreferences extends PreferenceActivity {

	private static final String PREFERENCES_NAME = "MAD_WIZUT_Preferences";
	// fields in checkboxes//
	private static final String LIST_FIELD_TYPE = "list_of_type";
	private static final String LIST_FIELD_OF_STUDY = "list_fos";
	private static final String LIST_GRADE_OF_STUDY = "list_stopien_studiow";
	private static final String LIST_FIELD_YEAR = "list_year";
	private static final String LIST_FIELD_GROUP = "list_group";

	private SharedPreferences preferences;
	private ListPreference listPreferenceField;
	private ListPreference listPreferenceOfStudy;
	private ListPreference listGradesOfStudy;
	private ListPreference listPreferenceYear;
	private ListPreference listPreferenceGroup;
	private Preference about_authors;

	private static final String TAG = "MAD WIZUT Widget";

	String[] tabGrupy;
	WidgetDownload wDownload = new WidgetDownload();

	// flaga do obslugi listenera
	boolean flaga = true;
	
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		preferences = getSharedPreferences(PREFERENCES_NAME,
				Activity.MODE_WORLD_READABLE);
		listPreferenceField = (ListPreference) findPreference("list_of_type");
		listPreferenceOfStudy = (ListPreference) findPreference("list_fos");
		listGradesOfStudy = (ListPreference) findPreference("list_stopien_studiow");
		listPreferenceYear = (ListPreference) findPreference("list_year");
		listPreferenceGroup = (ListPreference) findPreference("list_group");
		listPreferenceGroup.setOnPreferenceClickListener(groupButton);
		about_authors = findPreference("about");
		about_authors.setOnPreferenceClickListener(about);

		res = getApplicationContext().getResources();
		
		initPreferences();
	}

	public OnPreferenceClickListener about = new OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference about_button) {
			if (about_button.getTitle().equals("O nas")) {
				showDialog(0);
			}
			return false;
		}
	};

	public OnPreferenceClickListener groupButton = new OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference groupButton) {

			if (flaga == true) {

				preferences = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());

				// ----------------------------------------------------------------------
				// type of studies
				String listDefaultValueType = listPreferenceField
						.getEntryValues()[0].toString();
				String listValueOfType = preferences.getString(LIST_FIELD_TYPE,
						listDefaultValueType);
				listPreferenceField.setValue(listValueOfType);

				String rodzaj = preferences.getString("list_of_type",
						"nieokreslony_rodzaj");

				if (rodzaj.equals("1")) {
					rodzaj = "Stacjonarne";
					Log.i(TAG, "<<<stacjonarne>>>");
				} else {
					rodzaj = "Niestacjonarne";
					Log.i(TAG, "<<<niestacjonarne>>>");
				}
				// ----------------------------------------------------------------------
				// field of studies
				String listDefaultValueField = listPreferenceOfStudy
						.getEntryValues()[0].toString();
				String listValueOfStudy = preferences.getString(
						LIST_FIELD_OF_STUDY, listDefaultValueField);
				listPreferenceOfStudy.setValue(listValueOfStudy);

				String kierunek = preferences.getString("list_fos",
						"nieokreslony_kierunek");

				if (kierunek.equals("3")) {
					kierunek = "Informatyka";
					Log.i(TAG, "<<<Informatyka>>>");
				} else if (kierunek.equals("4")) {
					kierunek = "ZIP";
					Log.i(TAG, "<<<ZIP>>>");
				} else {
					kierunek = "Bioinformatyka";
					Log.i(TAG, "<<<BIOInf>>>");
				}
				// ----------------------------------------------------------------------
				// grades of studies
				String listDefaultValueGrade = listGradesOfStudy
						.getEntryValues()[0].toString();
				String listValueGrades = preferences.getString(
						LIST_GRADE_OF_STUDY, listDefaultValueGrade);
				listGradesOfStudy.setValue(listValueGrades);

				String s_stopien = preferences.getString(
						"list_stopien_studiow", "nieokreslony_stopien");

				if (s_stopien.equals("6")) {
					s_stopien = "1";
					Log.i(TAG, "<<<pierwszy stopien>>>");
				} else {
					s_stopien = "2";
					Log.i(TAG, "<<<drugi stopien>>>");
				}
				// convert string to int
				int stopien = Integer.parseInt(s_stopien);
				System.out.println("int stopien = " + stopien);
				// ----------------------------------------------------------------------
				// year of studies
				String listDefaultValueYear = listPreferenceYear
						.getEntryValues()[0].toString();
				String listValueYear = preferences.getString(LIST_FIELD_YEAR,
						listDefaultValueYear);
				listPreferenceYear.setValue(listValueYear);

				String s_year = preferences.getString("list_year",
						"nieokreslony_rok");

				if (s_year.equals("8")) {
					s_year = "1";
					Log.i(TAG, "<<<pierwszy rok>>>");
				} else if (s_year.equals("9")) {
					s_year = "2";
					Log.i(TAG, "<<<drugi rok>>>");
				} else if (s_year.equals("10")) {
					s_year = "3";
					Log.i(TAG, "<<<trzeci rok>>>");
				} else {
					s_year = "4";
					Log.i(TAG, "<<<czwarty rok>>>");
				}

				// convert string to int
				int rok = Integer.parseInt(s_year);
				// print out the value after the conversion
				System.out.println("int rok = " + rok);

				// ----------------------------------------------------------------------
				// group
				// ponowne wywolanie listenera w celu wyswietlenia grup za
				// pierwszym razem
				String listDefaultValueGroup = listPreferenceGroup
						.getEntryValues()[0].toString();
				String listValueGroup = preferences.getString(LIST_FIELD_GROUP,
						listDefaultValueGroup);
				listPreferenceGroup.setValue(listValueGroup);

				flaga = false;
				// this.onPreferenceClick(groupButton);
				groupButton.getOnPreferenceClickListener();

				// tablica stringow z grupami
				if ((tabGrupy = wDownload.getGroups(rodzaj, kierunek, stopien,
						rok)) != null) {

					Log.i(TAG, "<<GRUPY>" + Integer.toString(tabGrupy.length));
					
					if (tabGrupy.length > 0) {
						try {
							groupButton.getOnPreferenceClickListener();
							listPreferenceGroup.setEntries(tabGrupy);

							String[] entryValues = new String[tabGrupy.length];

							for (int i = 0; i < tabGrupy.length; i++) {
								Log.i(TAG, "<<tab grupy>> " + tabGrupy[i]);
								entryValues[i] = Integer.toString(i);

							}
							groupButton.getOnPreferenceClickListener();
							listPreferenceGroup.setEntryValues(entryValues);
							savePreferences();
						} catch (Exception e) {
							Log.i(TAG, e.toString());
						}

					}
					else
						Toast.makeText(getApplicationContext(), res.getString(R.string.no_groups), 3000).show();
					
				} else {
					Log.e(TAG, "Nie pobrano grup poprawnie !");
					return false;
				}

				return false;
			} else {
				flaga = true;
				return flaga;
			}

		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("MADWI Widget")
				.setMessage(
						res.getString(R.string.authors_list))
				.setPositiveButton("Zamknij",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
		return alert.create();
	}

	private void initPreferences() {
	}

	@Override
	protected void onPause() {
		super.onPause();
		savePreferences();
	}

	private void savePreferences() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(LIST_FIELD_TYPE, listPreferenceField.getValue());// save
		// type
		// of
		// studies
		editor.putString(LIST_FIELD_OF_STUDY, listPreferenceOfStudy.getValue());// save
		// field
		// of
		// studies
		editor.putString(LIST_GRADE_OF_STUDY, listGradesOfStudy.getValue());// save
		// grade
		// of
		// study
		editor.putString(LIST_FIELD_YEAR, listPreferenceYear.getValue());// save
		// year
		// of
		// studies
		editor.putString(LIST_FIELD_GROUP, listPreferenceGroup.getValue());// save
		// group

		editor.commit();

	}
}
