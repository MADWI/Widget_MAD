package mad.widget;

import mad.widget.activities.MyGroups;
import mad.widget.utils.Constans;
import mad.widget.utils.Intents;
import mad.widget.utils.SharedPrefUtils;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/** Glowna klasa widgetu */
public class MadWidgetProvider extends AppWidgetProvider {

	/**
	 * Zmienna do debuggowania.
	 */
	private static final String TAG = "AppWidgetProvider";

	/**
	 * Metoda wywolywana przy odswiezeniu widgetu przez system
	 * 
	 * @param context
	 *          kontekst aplikacji
	 * @param appWidgetManager
	 * 
	 * @param appWidgetIds
	 *          identyfikator instancji widgetu
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.i(TAG, "onUpdate");
		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				MadWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {

			// refresh onClick
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			// refresh OnClick
			Intent refreshIntent = Intents.actionRefresh(context);
			PendingIntent pendingIntentRefresh = Intents.createPendingService(
					context, refreshIntent);
			remoteViews.setOnClickPendingIntent(R.id.imb_odswiez,
					pendingIntentRefresh);

			// planChanges OnClick
			Intent planIntent = Intents.actionPlanChanges(context);
			PendingIntent pendingIntentPlan = Intents.createPendingActivity(context,
					planIntent);
			remoteViews
					.setOnClickPendingIntent(R.id.btZmianyPlanu, pendingIntentPlan);

			// settings OnClick
			Intent settingsIntent = Intents.actionSettings(context);
			PendingIntent pendingSettingsIntent = Intents.createPendingActivity(
					context, settingsIntent);
			remoteViews.setOnClickPendingIntent(R.id.imb_ustawienia,
					pendingSettingsIntent);

			// calendar OnClick
			Intent calendarIntent = Intents.actionCalendar(context);
			PendingIntent pendingCalendarIntent = Intents.createPendingActivity(
					context, calendarIntent);
			remoteViews.setOnClickPendingIntent(R.id.btnPokazKalendarz,
					pendingCalendarIntent);

			// open webapge OnClick

			Intent webpageIntent = Intents.actionWebpage(context);
			PendingIntent webpageSettingsIntent = Intents.createPendingActivity(
					context, webpageIntent);
			remoteViews.setOnClickPendingIntent(R.id.btnWeb, webpageSettingsIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
		// intent for calling update service
		Intent intent = new Intent(context, UpdateWidgetService.class);
		// start service
		context.startService(intent);
		Log.i(TAG, "onUpdate ended");

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/**
	 * Metoda, ktora jest wywolywana gdy zostanie usunieta z ekranu ostatnia
	 * instancja widgetu
	 * 
	 * @param context
	 *          kontekst aplikacji
	 */
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.i(TAG, "onDisabled");

	}

	/**
	 * Metoda, ktora jest wywolywana przy dodawaniu instancji widgetu na ekran
	 * 
	 * @param context
	 *          kontekst aplikacji
	 */
	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "onEnabled");
		super.onEnabled(context);

		if (!SharedPrefUtils.getSharedPreferences(context).contains(Constans.GROUP)) {

			Intent intent = new Intent(context, MyGroups.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	/**
	 * Obsluguje intencje odbierane od pozostalych metod
	 * 
	 * @param context
	 *          kontekst aplikacji
	 * @param intent
	 *          odebrana intencja aktualizacji
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				int[] appWidgetIds = extras
						.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				if (appWidgetIds != null && appWidgetIds.length > 0) {
					this.onUpdate(context, AppWidgetManager.getInstance(context),
							appWidgetIds);
				}
			}
		} else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			Bundle extras = intent.getExtras();
			if (extras != null
					&& extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
				final int appWidgetId = extras
						.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
			this.onEnabled(context);
		} else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
			this.onDisabled(context);
		}
	}
}
