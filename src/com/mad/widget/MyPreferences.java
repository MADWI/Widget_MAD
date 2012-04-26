package com.mad.widget;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class MyPreferences extends PreferenceActivity {
	
	private static final String PREFERENCES_NAME = "MAD_WIZUT_Preferences";
	//fields in checkboxes//
	private static final String LIST_FIELD_TYPE = "list_of_type";
	private static final String LIST_FIELD_OF_STUDY = "list_fos";
	private static final String LIST_FIELD_YEAR = "list_year";
	private static final String LIST_FIELD_GROUP = "list_group";
	private static final String CHECKBOX_FIELD = "checkbox";
	
	private SharedPreferences preferences;
	private ListPreference listPreferenceField;
	private ListPreference listPreferenceOfStudy;
	private ListPreference listPreferenceYear;
	private ListPreference listPreferenceGroup;
	private CheckBoxPreference checkBoxPreference;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_WORLD_READABLE);
		listPreferenceField = (ListPreference) findPreference("list_of_type");
		listPreferenceOfStudy = (ListPreference) findPreference("list_fos");
		listPreferenceYear = (ListPreference) findPreference("list_year");
		listPreferenceGroup = (ListPreference)findPreference("list_group");
		checkBoxPreference = (CheckBoxPreference) findPreference("checkbox");

		initPreferences();
	}

	private void initPreferences() {
	
		//type of studies
		String listDefaultValueType = listPreferenceField.getEntryValues()[0].toString();
		String listValueOfType = preferences.getString(LIST_FIELD_TYPE, listDefaultValueType);
		listPreferenceField.setValue(listValueOfType);
		//field of studies
		String listDefaultValueField = listPreferenceOfStudy.getEntryValues()[0].toString();
		String listValueOfStudy = preferences.getString(LIST_FIELD_OF_STUDY, listDefaultValueField);
		listPreferenceOfStudy.setValue(listValueOfStudy);
		//year of studies
		String listDefaultValueYear = listPreferenceYear.getEntryValues()[0].toString();
		String listValueYear = preferences.getString(LIST_FIELD_YEAR, listDefaultValueYear);
		listPreferenceYear.setValue(listValueYear);
		//group
		String listDefaultValueGroup = listPreferenceGroup.getEntryValues()[0].toString();
		String listValueGroup = preferences.getString(LIST_FIELD_GROUP, listDefaultValueGroup);
		listPreferenceGroup.setValue(listValueGroup);
		//auto-sync
		boolean checkBoxValue = preferences.getBoolean(CHECKBOX_FIELD, true);
		checkBoxPreference.setChecked(checkBoxValue);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		savePreferences();
	}
	
	private void savePreferences() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(LIST_FIELD_TYPE, listPreferenceField.getValue());//save type of studies
		editor.putString(LIST_FIELD_OF_STUDY, listPreferenceOfStudy.getValue());//save field of studies
		editor.putString(LIST_FIELD_YEAR, listPreferenceYear.getValue());//save year of studies
		editor.putString(LIST_FIELD_GROUP, listPreferenceGroup.getValue());//save group
		editor.putBoolean(CHECKBOX_FIELD, checkBoxPreference.isChecked());//auto-sync
		editor.commit();

	}
}