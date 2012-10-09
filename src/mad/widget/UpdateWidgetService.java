package mad.widget;

import mad.widget.connections.GetPlanChanges;
import mad.widget.connections.HttpConnect;
import mad.widget.connections.PlanDownloader;
import mad.widget.connections.WeekParityChecker;
import mad.widget.models.MessagePlanChanges;
import mad.widget.utils.Constans;
import mad.widget.utils.Intents;
import mad.widget.utils.SharedPrefUtils;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpdateWidgetService extends IntentService {

	private static final String TAG = "UpdateWidgetService";

	private final WeekParityChecker checker = new WeekParityChecker();
	private final GetPlanChanges PlanChanges = new GetPlanChanges();
	private MessagePlanChanges lastMessage = new MessagePlanChanges();

	// fields Strings
	private String userGroup = " ";
	private String userStudiesType = " ";

	public UpdateWidgetService() {
		super("UpdateWidgetService");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStart");

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());
		ComponentName thisWidget = new ComponentName(
				this.getApplicationContext(), MadWidgetProvider.class.getName());

		// remote view to show progress bar
		RemoteViews remoteViewsStarting = new RemoteViews(this
				.getApplicationContext().getPackageName(),
				R.layout.widget_layout);

		remoteViewsStarting.setViewVisibility(R.id.ProgressBarLayout,
				View.VISIBLE);
		// finally update widget by RemoteView
		appWidgetManager.updateAppWidget(thisWidget, remoteViewsStarting);

		// get SharedPreferences
		SharedPreferences ustawienia = SharedPrefUtils
				.getSharedPreferences(this.getApplicationContext());

		// get user group
		userGroup = ustawienia.getString(Constans.GROUP,
				getString(R.string.empty_group));

		// get user studies type
		userStudiesType = ustawienia.getString(Constans.TYPE, "");

		Log.d(TAG, "Grupa " + userGroup);
		Log.d(TAG, "Typ " + userStudiesType);

		// get week parity and plan changes
		String currentWeekParity = "";
		if (HttpConnect.isOnline(this.getApplicationContext())) {

			try {
				Log.i(TAG, "Getting week parity...");

				currentWeekParity = checker.getParity();
				Log.i(TAG, "Week parity = " + currentWeekParity);

				Log.i(TAG, "Getting last plan change...");
				lastMessage = PlanChanges.getLastMessage();
				if (lastMessage == null) {
					lastMessage = new MessagePlanChanges();
					lastMessage.setBody(getString(R.string.no_messages));

				} else {
					Log.i(TAG, "last plan change= " + lastMessage.getTitle()
							+ "success");

					String temp = lastMessage.getTitle();
					temp = temp.substring(0, 1).toUpperCase()
							+ temp.substring(1, temp.length());

					lastMessage.setTitle(temp);

				}
			} catch (NullPointerException e) {

			}

		}

		// remote view to update widget layout
		RemoteViews remoteViews = new RemoteViews(this.getApplicationContext()
				.getPackageName(), R.layout.widget_layout);

		// download plan if no exist and setonclick

		if (PlanDownloader.planExistsAndNew(this.getApplicationContext(),
				userStudiesType, userGroup)) {
			Log.d(TAG, "Plan istnieje na dysku");

			// plan show onClick
			Intent showPlanIntent = Intents.actionShowPlan(
					this.getApplicationContext(), userGroup);

			PendingIntent pendingIntentPlan = Intents.createPendingActivity(
					this.getApplicationContext(), showPlanIntent);
			remoteViews.setOnClickPendingIntent(R.id.btnPobierzPlan,
					pendingIntentPlan);

		} else {
			Log.d(TAG, "Plan nie isteniejê, pobieram...");
			if (HttpConnect.isOnline(this.getApplicationContext())) {

				if (PlanDownloader.downloadPlan(this.getApplicationContext(),
						userStudiesType, userGroup)) {

					// plan show onClick
					Intent showPlanIntent = Intents.actionShowPlan(
							this.getApplicationContext(), userGroup);

					PendingIntent pendingIntentPlan = Intents
							.createPendingActivity(
									this.getApplicationContext(),
									showPlanIntent);
					remoteViews.setOnClickPendingIntent(R.id.btnPobierzPlan,
							pendingIntentPlan);

					Log.d(TAG, "Pobrano plan");
				} else {
					Toast.makeText(this.getApplicationContext(),
							this.getString(R.string.cannot_download_plan),
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "Niepobrano planu ");
				}
			}

		}
		// set user group
		remoteViews.setTextViewText(R.id.btnPobierzPlan, userGroup);

		// set week parity
		if (!currentWeekParity.equals("")) {
			remoteViews.setTextViewText(R.id.tv_tydzien, currentWeekParity);
			SharedPrefUtils.saveString(ustawienia, Constans.WEEK_PARITY,
					currentWeekParity);
		} else {
			remoteViews.setTextViewText(R.id.tv_tydzien, SharedPrefUtils
					.loadString(ustawienia, Constans.WEEK_PARITY));
		}

		// show last plan change title
		if (!lastMessage.getTitle().equals("")) {
			remoteViews.setTextViewText(R.id.tv_zmiany_tytul,
					lastMessage.getTitle());
			SharedPrefUtils.saveString(ustawienia, Constans.TITLE_PLAN_CHANGES,
					lastMessage.getTitle());
		} else
			remoteViews.setTextViewText(R.id.tv_zmiany_tytul, SharedPrefUtils
					.loadString(ustawienia, Constans.TITLE_PLAN_CHANGES));

		// set plan changes body
		// if there is no messages
		if (lastMessage.getBody().equals(getString(R.string.no_messages))) {
			remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
					lastMessage.getBody());
			// if messages downloaded successfull
		} else if (!lastMessage.getBody().equals("")) {
			String bodyLastMessage = lastMessage.getBody().substring(0, 65)
					+ "...";
			remoteViews.setTextViewText(R.id.tv_zmiany_tresc, bodyLastMessage);

			SharedPrefUtils.saveString(ustawienia, Constans.BODY_PLAN_CHANGES,
					bodyLastMessage);
			if (lastMessage.getBody().equals(getString(R.string.no_messages))) {
				remoteViews.setTextViewText(R.id.tv_zmiany_tresc,
						bodyLastMessage);
			}
			// if have no Internet connectivity
		} else {
			remoteViews.setTextViewText(R.id.tv_zmiany_tresc, SharedPrefUtils
					.loadString(ustawienia, Constans.BODY_PLAN_CHANGES));
		}

		// hide progress bar
		remoteViews.setViewVisibility(R.id.ProgressBarLayout, View.INVISIBLE);

		// finally update widget by RemoteView
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		Log.i(TAG, "onStart ended");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

}
