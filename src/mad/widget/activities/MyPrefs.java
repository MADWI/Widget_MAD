package mad.widget.activities;

import mad.widget.R;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Klasa definiujaca widok okna ustawien
 */
public class MyPrefs extends PreferenceActivity {

    /** Metoda wywolywana przy starcie aplikacji */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.prefs);

	final Preference about = (Preference) findPreference("about");

	about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

	    public boolean onPreferenceClick(Preference preference) {

		final Dialog dialog = new Dialog(MyPrefs.this);
		dialog.setContentView(R.layout.dialog_info_layout);
		dialog.setTitle(getString(R.string.about_title));

		Button dialogButton = (Button) dialog
			.findViewById(R.id.btnOkDialog);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			dialog.dismiss();
		    }
		});

		dialog.show();
		return true;
	    }
	});

	final Preference plans = (Preference) findPreference("plans");

	plans.setOnPreferenceClickListener(new OnPreferenceClickListener() {

	    public boolean onPreferenceClick(Preference preference) {
		Intent intent = new Intent(MyPrefs.this, RemovePlans.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyPrefs.this.startActivity(intent);
		return true;
	    }
	});

	final Preference groups = (Preference) findPreference("groups");

	groups.setOnPreferenceClickListener(new OnPreferenceClickListener() {

	    public boolean onPreferenceClick(Preference preference) {
		Intent intent = new Intent(MyPrefs.this, MyGroups.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyPrefs.this.startActivity(intent);

		return true;
	    }
	});

    }
}
