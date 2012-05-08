package com.mad.widget;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class MadWIWidgetActivity extends AppWidgetProvider {

	public static final String ACTION_WIDGET_GET_PLAN_CHANGES = "PlanChangesWidget";
	public static String ACTION_WIDGET_REFRESH = "ActionRefreshWidget";
	public static String ACTION_WIDGET_PREFERENCES = "ActionPreferencesWidget";

	private static final String PREFERENCES_NAME = "MAD_WIZUT_Preferences";
	private static final String TAG = "MAD WIZUT Widget";
	private SharedPreferences ustawienia;

	static final long refresh_seconds = 10;
	private Boolean running = false;
	private Boolean autoRefresh;
	
	private WeekParityChecker checker = new WeekParityChecker();
	private String currentWeekStatus;
	
	// obiekt do pobierania zmian w planie
	private final GetPlanChanges PlanChanges = new GetPlanChanges();
	// ostatnia zmiana w planie
	private MessagePlanChanges lastMessage = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		
		// pobarnie chceckoboxa z info o automatycznej aktualizacji z shared
		// preferences
		ustawienia = context.getSharedPreferences(PREFERENCES_NAME,
				Activity.MODE_WORLD_READABLE);
		autoRefresh = ustawienia.getBoolean("checkbox", true);

		Log.e(TAG, "<<< checkbox =" + autoRefresh.toString());

		if (autoRefresh == true) {
			new Timer().scheduleAtFixedRate(new Aktualizacja(context,
					appWidgetManager), 10, refresh_seconds * 1000);
			Log.e(TAG, "<<< AUTOREFRESH ON >>>");
		} else {
			Log.e(TAG, "<<< AUTOREFRESH OFF  >>>");
			// jakies przerwanie automatycznej aktualizacji

		}

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget1);

		// onclik zmiany w planie
		Intent changesIntent = new Intent(context, PlanChangesActivity.class);
		changesIntent.setAction(ACTION_WIDGET_GET_PLAN_CHANGES);
		PendingIntent changesPendingIntent = PendingIntent.getActivity(context,
				0, changesIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btZmianyPlanu,
				changesPendingIntent);

		// onclik ustawienia
		Intent configIntent = new Intent(context, MyPreferences.class);
		configIntent.setAction(ACTION_WIDGET_PREFERENCES);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context,
				0, configIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.imb_ustawienia,
				configPendingIntent);

		// onclick refresh
		Intent active = new Intent(context, MadWIWidgetActivity.class);
		active.setAction(ACTION_WIDGET_REFRESH);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.imb_odswiez,
				actionPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();

		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {

			if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
				Log.e(TAG, "<<< onClick Refresh Button  >>>");

				if (running == false)
					new Timer().schedule(new Aktualizacja(context,
							AppWidgetManager.getInstance(context)),10);
			} else {
			}
			super.onReceive(context, intent);
		}
	}

	public boolean isOnline(Context ctx) {
		Log.e(TAG, "<<< is Online ? >>>");
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private class Aktualizacja extends TimerTask {

		private final RemoteViews remoteViews;
		private final ComponentName thisWidget;
		private final AppWidgetManager appWidgetManager;
		private final Context ctx;
		private final Resources res;

		public Aktualizacja(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget1);
			thisWidget = new ComponentName(context, MadWIWidgetActivity.class);
			ctx = context;
			res = context.getResources();
		}

		@Override
		public void run() {

			Log.e(TAG, "<<< RUN refresh>>");
			running = true;
			
			// pobranie daty zgodnie z ustawieniami jezyka telefonu
			String currentDateTimeString = DateFormat.getDateInstance().format(
					new Date());
			remoteViews.setTextViewText(R.id.tv_data, currentDateTimeString
					+ " r.");
			
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

			// sprawdzanie czy jest połaczenie z Internetem
			if (isOnline(ctx)) {
				currentWeekStatus= checker.getParity(); 
				remoteViews.setTextViewText(R.id.tv_tydzien, currentWeekStatus);
				
				// pokazuje i chowam layout z progrsse barem
				// poniewaz on sam nie obsluguje ustawiania widocznosci
				remoteViews.setViewVisibility(R.id.ProgressBarLayout,
						View.VISIBLE);

				remoteViews.setTextViewText(R.id.tv_zmiany_tytul, "");
				remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
						res.getString(R.string.load_plan_changes));
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);

				Log.e(TAG, "<<< getlast message  with AsyncTask>>>");

				lastMessage = PlanChanges.getLastMessage();

				if (lastMessage != null) {
					remoteViews.setTextViewText(R.id.tv_zmiany_tytul,
							lastMessage.getTitle());
					String bodyLastMessage = lastMessage.getBody().substring(0,
							110)
							+ "...";
					remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
							bodyLastMessage);

				} // schowanie layoutu z progress barem
				remoteViews.setViewVisibility(R.id.ProgressBarLayout,
						View.INVISIBLE);

				// zaktualizowanie widoku
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);

			} else {
				remoteViews.setViewVisibility(R.id.ProgressBarLayout,
						View.INVISIBLE);
				remoteViews.setTextViewText(R.id.tv_zmiany_tytul,
						res.getString(R.string.no_Internet));
				remoteViews.setTextViewText(R.id.tv_zmiany_tresc, "");
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			}

			running = false;
		}
	}
}
