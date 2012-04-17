package com.mad.wi.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class MadWIWidgetActivity extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);
        views.setOnClickPendingIntent(R.id.widget3label, pendingIntent);
        
        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.widget1);
        views.setOnClickPendingIntent(R.id.widget4label, pendingIntent);

    }

}