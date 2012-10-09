package mad.widget.utils;

import java.io.File;

import mad.widget.UpdateWidgetService;
import mad.widget.activities.MyPrefs;
import mad.widget.activities.PlanChangesActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

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

	public static final Intent actionSettings(final Context context) {
		final Intent intent = new Intent(context, MyPrefs.class);
		return intent;

	}

	public static final Intent actionWebpage(final Context context) {
		final Uri uri = Uri.parse("http://www.mad.zut.edu.pl");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		return intent;

	}

	public static final Intent actionShowPlan(final Context context,
			String grupa) {
		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot + Constans.PLAN_FOLDER + "/" + grupa
				+ ".pdf");

		if (file.exists()) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			return intent;
		} else
			return null;

	}

	public static PendingIntent createPendingActivity(final Context context,
			final Intent intent) {
		return PendingIntent.getActivity(context, intent.hashCode(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent createPendingService(final Context context,
			final Intent intent) {
		return PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
