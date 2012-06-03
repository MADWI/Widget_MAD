package com.mad.widget.activities;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.mad.widget.R;
import com.mad.widget.http.GetPlanChanges;
import com.mad.widget.http.MessagePlanChanges;
import com.mad.widget.http.WeekParityChecker;

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

    private static final String TAG = "MAD WIZUT Widget";

    static final long refresh_seconds = 43200;
    private Boolean running = false;

    /*
     * Week parity
     */
    private final WeekParityChecker checker = new WeekParityChecker();
    private String currentWeekStatus;

    /*
     * Plan Changes
     */
    private final GetPlanChanges PlanChanges = new GetPlanChanges();
    private MessagePlanChanges lastMessage = null;

    /*
     * Shared Preferences
     */
    private static final String PREFERENCES_NAME = "MAD_WIZUT_Preferences";
    private SharedPreferences preferences;

    /*
     * Resources
     */
    private Resources res;

    @Override
    public void onUpdate(final Context context,
	    final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	super.onUpdate(context, appWidgetManager, appWidgetIds);

	/*
	 * Get Resources
	 */
	res = context.getResources();

	/*
	 * Schedule Timer
	 */
	new Timer().scheduleAtFixedRate(new Aktualizacja(context,
		appWidgetManager), 10, refresh_seconds * 1000);

	/*
	 * Attach shared preferences
	 */
	preferences = context.getSharedPreferences(PREFERENCES_NAME,
		getResultCode());

	/*
	 * Get package name
	 */
	final String sPackageName = context.getPackageName();

	/*
	 * Connect remote view with widget
	 */
	RemoteViews oRemoteViews = new RemoteViews(sPackageName,
		R.layout.widget1);
	/*
	 * Set OnClick Plan Changes
	 */
	Intent changesIntent = new Intent(context, PlanChangesActivity.class);
	changesIntent.setAction(ACTION_WIDGET_GET_PLAN_CHANGES);
	PendingIntent changesPendingIntent = PendingIntent.getActivity(context,
		0, changesIntent, 0);
	oRemoteViews.setOnClickPendingIntent(R.id.btZmianyPlanu,
		changesPendingIntent);

	/*
	 * Set OnClick Preferences
	 */
	Intent configIntent = new Intent(context, MyPreferences.class);
	configIntent.setAction(ACTION_WIDGET_PREFERENCES);
	PendingIntent configPendingIntent = PendingIntent.getActivity(context,
		0, configIntent, 0);
	oRemoteViews.setOnClickPendingIntent(R.id.imb_ustawienia,
		configPendingIntent);

	/*
	 * Set OnClick Refresh
	 */
	Intent active = new Intent(context, MadWIWidgetActivity.class);
	active.setAction(ACTION_WIDGET_REFRESH);
	PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
		0, active, 0);
	oRemoteViews.setOnClickPendingIntent(R.id.imb_odswiez,
		actionPendingIntent);

	/*
	 * Update the remote views
	 */

	appWidgetManager.updateAppWidget(appWidgetIds, oRemoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
	super.onReceive(context, intent);

	final String action = intent.getAction();

	if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
	    final int appWidgetId = intent.getExtras().getInt(
		    AppWidgetManager.EXTRA_APPWIDGET_ID,
		    AppWidgetManager.INVALID_APPWIDGET_ID);
	    if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
		onDeleted(context, new int[] { appWidgetId });
	    }
	} else {

	    if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
		Log.i(TAG, "<<< onClick Refresh Button  >>>");

		if (running == false) {
		    new Timer().schedule(new Aktualizacja(context,
			    AppWidgetManager.getInstance(context)), 10);
		}
	    } else {
	    }
	    super.onReceive(context, intent);
	}
    }

    public boolean isOnline(Context ctx) {
	Log.i(TAG, "<<< is Online ? >>>");
	ConnectivityManager cm = (ConnectivityManager) ctx
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni = cm.getActiveNetworkInfo();
	if (ni != null && ni.isAvailable() && ni.isConnected()) {
	    return true;
	} else {
	    Log.e(TAG, "<<< is Online ? NO CONNECTION >>>");
	    return false;
	}
    }

    private class Aktualizacja extends TimerTask {

	private final RemoteViews remoteViews;
	private final ComponentName thisWidget;
	private final AppWidgetManager appWidgetManager;
	private final Context ctx;

	public Aktualizacja(Context context, AppWidgetManager appWidgetManager) {
	    this.appWidgetManager = appWidgetManager;
	    remoteViews = new RemoteViews(context.getPackageName(),
		    R.layout.widget1);
	    thisWidget = new ComponentName(context, MadWIWidgetActivity.class);
	    ctx = context;
	}

	@Override
	public void run() {

	    Log.i(TAG, "<<< RUN refresh>>");
	    running = true;

	    /*
	     * Get system date
	     */
	    String currentDateTimeString = DateFormat.getDateInstance().format(
		    new Date());
	    remoteViews.setTextViewText(R.id.tv_data, currentDateTimeString
		    + " r.");
	    /*
	     * Do it only if have connectivity
	     */
	    if (isOnline(ctx)) {

		/*
		 * Set progress bar and default text in TextView's
		 */
		remoteViews.setViewVisibility(R.id.ProgressBarLayout,
			View.VISIBLE);
		remoteViews.setTextViewText(R.id.tv_zmiany_tytul, "");
		remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
			res.getString(R.string.load_plan_changes));
		remoteViews.setTextViewText(R.id.btnPobierzPlan,
			preferences.getString("list_group", "Brak grupy"));

		/*
		 * Get week parity
		 */
		Log.i(TAG, "<<< get week parity >>>");
		currentWeekStatus = checker.getParity();
		remoteViews.setTextViewText(R.id.tv_tydzien, currentWeekStatus);

		/*
		 * Get last plan changes
		 */
		Log.i(TAG, "<<< getlast messages (plan changes)>>>");
		lastMessage = PlanChanges.getLastMessage();

		if (lastMessage != null) {
		    remoteViews.setTextViewText(R.id.tv_zmiany_tytul,
			    lastMessage.getTitle());
		    String bodyLastMessage = lastMessage.getBody().substring(0,
			    110)
			    + "...";
		    remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
			    bodyLastMessage);
		}

		/*
		 * Unset progress bar
		 */
		remoteViews.setViewVisibility(R.id.ProgressBarLayout,
			View.INVISIBLE);

		/*
		 * Update all changes
		 */
		Log.i(TAG, "<<< remoteview updated>>>");
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);

	    } else {
		/*
		 * If have no connectivity set info about that in widget
		 * TextView's
		 */
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
