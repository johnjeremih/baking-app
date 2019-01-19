package com.johnny.john.bakingapp.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.johnny.john.bakingapp.model.Recipe;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {


    private String url;
    private  static String TAG  =  RecipeLoader.class.getSimpleName();
    private Context context;

    public RecipeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }



    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {

        if (url == null) {

            return null;
        }

        List<Recipe> recipes = BakingJson.fetchRecipeData(url);
        return recipes;
    }


}



