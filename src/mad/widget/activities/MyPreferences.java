package mad.widget.activities;

import mad.widget.R;
import mad.widget.connections.WidgetDownload;
import mad.widget.utils.Constans;
import mad.widget.utils.SharedPrefUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

public class MyPreferences extends PreferenceActivity {

	private static final String TAG = "MyPreferences";

	/** Obiekt w ktorym zapisywane sa ustawiena aplikacji */
	private static SharedPreferences preferences;

	/** Pola typu listPreference ktore wyswietlaja informacje o studiach */
	private ListPreference listPreferenceField;
	private ListPreference listPreferenceOfStudy;
	private ListPreference listGradesOfStudy;
	private ListPreference listPreferenceYear;
	private ListPreference listPreferenceGroup;
	private Preference about_authors;

	String[] tabGrupy;

	WidgetDownload wDownload = new WidgetDownload(null);

	/** flaga do obslugi listenera */
	boolean flaga = true;

	/**
	 * Widget ID
	 */
	private int widgetID;

	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// invalid widget id, finish
		if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
			finish();

		// If the user press BACK, do not add any widget.

		setResult(RESULT_CANCELED);

		addPreferencesFromResource(R.xml.preferences);
		preferences = SharedPrefUtils
				.getSharedPreferences(getApplicationContext());

		listPreferenceField = (ListPreference) findPreference("list_of_type");
		listPreferenceOfStudy = (ListPreference) findPreference("list_fos");
		listGradesOfStudy = (ListPreference) findPreference("list_stopien_studiow");
		listPreferenceYear = (ListPreference) findPreference("list_year");
		listPreferenceYear.setOnPreferenceClickListener(groupButton);
		listPreferenceGroup = (ListPreference) findPreference("list_group");
		listPreferenceGroup.setOnPreferenceClickListener(groupButton);
		about_authors = findPreference("about");
		about_authors.setOnPreferenceClickListener(about);

		res = getApplicationContext().getResources();

		initPreferences();
	}

	public OnPreferenceClickListener about = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference about_button) {
			if (about_button.getTitle().equals("O nas"))
				showDialog(0);
			return false;
		}
	};

	public OnPreferenceClickListener groupButton = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference groupButton) {

			if (flaga == true) {

				// ----------------------------------------------------------------------
				// type of studies
				String listDefaultValueType = listPreferenceField
						.getEntryValues()[0].toString();
				String listValueOfType = preferences.getString(
						Constans.LIST_FIELD_TYPE, listDefaultValueType);
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
						Constans.LIST_FIELD_OF_STUDY, listDefaultValueField);
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
						Constans.LIST_GRADE_OF_STUDY, listDefaultValueGrade);
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
				String listValueYear = preferences.getString(
						Constans.LIST_FIELD_YEAR, listDefaultValueYear);
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

				String listDefaultValueGroup = listPreferenceGroup
						.getEntryValues()[0].toString();
				String listValueGroup = preferences.getString(
						Constans.LIST_FIELD_GROUP, listDefaultValueGroup);
				listPreferenceGroup.setValue(listValueGroup);

				/**
				 * ponowne wywolanie listenera w celu wyswietlenia grup za
				 * pierwszym razem
				 */
				flaga = false;
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
								Log.i(TAG, "<<tab grpy>> " + tabGrupy[i]);
								entryValues[i] = Integer.toString(i);

							}
							groupButton.getOnPreferenceClickListener();
							listPreferenceGroup.setEntryValues(entryValues);
							savePreferences();
						} catch (Exception e) {
							Log.i(TAG, e.toString());
						}

					} else
						Toast.makeText(getApplicationContext(),
								res.getString(R.string.no_groups),
								Toast.LENGTH_SHORT).show();

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
				.setMessage(res.getString(R.string.authors_list))
				.setPositiveButton("Zamknij",
						new DialogInterface.OnClickListener() {

							@Override
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
		Log.i(TAG, "onPause");
		savePreferences();
	}

	private void savePreferences() {
		Log.i(TAG, "savePreferences");
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Constans.LIST_FIELD_TYPE,
				listPreferenceField.getValue());// save
		// type
		// of
		// studies
		editor.putString(Constans.LIST_FIELD_OF_STUDY,
				listPreferenceOfStudy.getValue());// save
		// field
		// of
		// studies
		editor.putString(Constans.LIST_GRADE_OF_STUDY,
				listGradesOfStudy.getValue());// save
		// grade
		// of
		// study
		editor.putString(Constans.LIST_FIELD_YEAR,
				listPreferenceYear.getValue());// save
		// year
		// of
		// studies
		editor.putString(Constans.LIST_FIELD_GROUP,
				listPreferenceGroup.getValue());// save
		// group

		editor.commit();

		final Context context = MyPreferences.this;
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(),
				MyPreferences.class.getName());

		// launch AppWidgetProvider intent
		Intent firstUpdate = new Intent(context, MyPreferences.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		firstUpdate.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		firstUpdate
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		context.sendBroadcast(firstUpdate);
		Log.i(TAG, "onUpdate broadcast sent");

		// return the widget ID
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();

	}

}
