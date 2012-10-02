package mad.widget.activities;

import mad.widget.R;
import mad.widget.connections.HttpConnect;
import mad.widget.connections.PlanDownloader;

import mad.widget.utils.Constans;
import mad.widget.utils.Intents;
import mad.widget.utils.SharedPrefUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MyPrefs extends Activity implements OnClickListener {

	private static final String TAG = "MyPrefs";
	private SharedPreferences preferences;
	private Resources res;

	private AsyncTaskDownloadGroups downloadGroups;

	// view elements
	private Button next;
	private Button cancel;
	private Spinner spinType;
	private Spinner spinDegree;
	private Spinner spinLevel;
	private Spinner spinYear;
	private Spinner spinGroup;
	private ProgressDialog progresDialog;

	private RelativeLayout pick_studies;
	private RelativeLayout pic_group;

	// selected items
	private String rodzaj;
	private String kierunek;
	private int stopien;
	private int rok;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_prefs);
		Log.i(TAG, "onCreate");
		/*
		 * Connect object with view elements
		 */
		next = (Button) findViewById(R.id.btnNextPrefs);
		next.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.btnCancelPrefs);
		cancel.setOnClickListener(this);

		spinType = (Spinner) findViewById(R.id.spinType);
		spinDegree = (Spinner) findViewById(R.id.spinDegree);
		spinLevel = (Spinner) findViewById(R.id.spinLevel);
		spinYear = (Spinner) findViewById(R.id.spinYear);
		spinGroup = (Spinner) findViewById(R.id.spinGroup);
		pick_studies = (RelativeLayout) findViewById(R.id.pick_studies_layout);
		pic_group = (RelativeLayout) findViewById(R.id.pic_group_layout);

		/*
		 * get SharedPreferences
		 */
		preferences = SharedPrefUtils
				.getSharedPreferences(getApplicationContext());
		/*
		 * get Resources
		 */
		res = getApplicationContext().getResources();

	

	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");
		switch (v.getId()) {
		case R.id.btnNextPrefs:
			Log.i(TAG, "onClick button next");

			if (pick_studies.getVisibility() == View.VISIBLE) {
				rodzaj = spinType.getSelectedItem().toString();
				kierunek = spinDegree.getSelectedItem().toString();
				stopien = spinLevel.getSelectedItemPosition() + 1;
				rok = spinYear.getSelectedItemPosition() + 1;

				Log.d(TAG,
						rodzaj + " " + kierunek + " "
								+ Integer.toString(stopien) + " "
								+ Integer.toString(rok));
				
				downloadGroups = new AsyncTaskDownloadGroups();
				downloadGroups.execute(this);
			} else {
				// second click , save group/type and refresh layout

				SharedPrefUtils.saveString(preferences, Constans.GROUP,
						spinGroup.getSelectedItem().toString());

				SharedPrefUtils.saveString(preferences, Constans.TYPE, spinType
						.getSelectedItem().toString());

				Intent refresh = Intents.actionRefresh(this);
				startService(refresh);

				finish();
			}

			break;
		case R.id.btnCancelPrefs:
			finish();
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_prefs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "onOptionsItemSelected");

		switch (item.getItemId()) {
		case R.id.menu_info:

			// custom dialog
			final Dialog dialog = new Dialog(this);
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

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class AsyncTaskDownloadGroups extends
			AsyncTask<Context, Void, String[]> {

		private Context ctx;

		@Override
		protected String[] doInBackground(Context... params) {
			Log.i(TAG, "doInBackground");
			ctx = params[0];

			String[] tempGroups = null;
			if (HttpConnect.isOnline(getApplicationContext()) == true) {
				tempGroups = PlanDownloader.getGroups(rodzaj, kierunek,
						stopien, rok);
			}
			return tempGroups;
		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			progresDialog = ProgressDialog.show(MyPrefs.this,
					res.getString(R.string.download_groups_title),
					res.getString(R.string.refreshing_body), true, true);
			progresDialog.setCancelable(false);

		}

		@Override
		protected void onPostExecute(String[] result) {
			Log.i(TAG, "onPostExecute");
			progresDialog.dismiss();
			
			if (result != null && result.length > 0) {
				pick_studies.setVisibility(View.INVISIBLE);
				pic_group.setVisibility(View.VISIBLE);

				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
						ctx, android.R.layout.simple_spinner_item, result);

				spinnerArrayAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				spinGroup.setAdapter(spinnerArrayAdapter);

			
			} else
				Toast.makeText(ctx,
						ctx.getString(R.string.cannot_download_groups),
						Toast.LENGTH_SHORT).show();

		}

	}

}
