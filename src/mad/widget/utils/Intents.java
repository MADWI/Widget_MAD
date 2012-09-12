package mad.widget.utils;

import mad.widget.UpdateWidgetService;
import mad.widget.activities.MyPreferences;
import mad.widget.activities.PlanChangesActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

public class Intents {
	public static final Intent actionRefresh(final Context context) {
		final Intent intent = new Intent(context, UpdateWidgetService.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		return intent;
	}

	public static final Intent actionPlanChanges(final Context context) {
		final Intent intent = new Intent(context, PlanChangesActivity.class);
		intent.setAction(Constans.ACTION_WIDGET_GET_PLAN_CHANGES);
		return intent;

	}

	public static final Intent actionSettings(final Context context, int appWidgetId) {
		final Intent intent = new Intent(context, MyPreferences.class);
		intent.putExtra(AppWidgetManager. EXTRA_APPWIDGET_ID,appWidgetId);
		return intent;

	}

	public static PendingIntent createPendingActivity(final Context context,
			final Intent intent) {
		return PendingIntent.getActivity(context, intent.hashCode(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	// FIXME: Hash codes are a hack here since it wastes pendingintents.
	public static PendingIntent createPendingService(final Context context,
			final Intent intent) {
		return PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
