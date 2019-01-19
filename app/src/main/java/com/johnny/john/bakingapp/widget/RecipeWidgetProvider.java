package com.johnny.john.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.activity.IngredientsActivity;
import com.johnny.john.bakingapp.activity.MainActivity;
import com.johnny.john.bakingapp.data.Contractor;
import com.johnny.john.bakingapp.model.Ingredients;

import org.w3c.dom.Text;

public class RecipeWidgetProvider extends AppWidgetProvider   {


    public static void sendRefreshBroadcast(Context context ) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));

        context.sendBroadcast(intent);
    }



    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        assert action != null;
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.collection_widget
            );



            String title = context.getString(R.string.appwidget_text);
            Intent intent = new Intent(context, WidgetRemoteViewService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);
            views.setTextViewText(R.id.text_widget_name_view, title );
            views.setEmptyView(R.id.widgetListView, R.id.empty_view);



            //Open App main screen
            Intent appIntent = new Intent(context, MainActivity.class);
           PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           views.setOnClickPendingIntent(R.id.empty_text_recipe_trigger, appPendingIntent);


            Intent ingred = new Intent(context, IngredientsActivity.class);
            PendingIntent appPendingIngre = PendingIntent.getActivity(context, 0, ingred, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.text_widget_name_view, appPendingIngre);

            appWidgetManager.updateAppWidget(appWidgetId, views);
    }





    }



}

