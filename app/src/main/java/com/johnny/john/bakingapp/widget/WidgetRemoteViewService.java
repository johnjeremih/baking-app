package com.johnny.john.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.data.Contractor;
import com.johnny.john.bakingapp.model.Recipe;

import static com.johnny.john.bakingapp.data.Contractor.BASE_CONTENT_URI;
import static com.johnny.john.bakingapp.data.Contractor.PATH_WIDGET;


public class WidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {



        return new RecipeRemoteFactory(this.getApplicationContext(),intent);
    }




        public class RecipeRemoteFactory implements RemoteViewsFactory {

        Context mContext;
        Cursor mCursor;


        RecipeRemoteFactory(Context applicationContext, Intent intent) {

            mContext = applicationContext;
        }




        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if (mCursor != null){
                mCursor.close();

            }

            final long identityToken = Binder.clearCallingIdentity();

            Uri RECIPE_LIST_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WIDGET).build();
            mCursor = mContext.getContentResolver().query(
                    RECIPE_LIST_URI,
                    null,
                    null,
                    null,
                    Contractor.IngredientsEntry._ID + " DESC");


            Binder.restoreCallingIdentity(identityToken);

        }

        @Override
        public void onDestroy() {
            if (mCursor != null){
                mCursor.close();
            }

        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {





            if (position == AdapterView.INVALID_POSITION ||
                    mCursor == null || !mCursor.moveToPosition(position)){
                return null;
            }


            String recipeName = mCursor.getString(mCursor.getColumnIndex(Contractor.IngredientsEntry.COLUMN_RECIPE_NAME));
            String ingredient = mCursor.getString(mCursor.getColumnIndex(Contractor.IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT));
            String measure = mCursor.getString(mCursor.getColumnIndex(Contractor.IngredientsEntry.COLUMN_INGREDIENT_MEASURE));
            String quantity = mCursor.getString(mCursor.getColumnIndex(Contractor.IngredientsEntry.COLUMN_INGREDIENT_QUANTITY));


            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.collection_widget_list_item);

            remoteViews.setTextViewText(R.id.text_widget_name_view,recipeName);
            remoteViews.setTextViewText(R.id.ingredient_widget_tv, ingredient);
            remoteViews.setTextViewText(R.id.measure_widget_tv, measure);
            remoteViews.setTextViewText(R.id.quantity_widget_tv, quantity);

            remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view);



            return remoteViews;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return mCursor == null ? 0 : mCursor.getCount() ;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }


}
