package com.johnny.john.bakingapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.adapters.RecipeAdapter;
import com.johnny.john.bakingapp.data.RecipeLoader;
import com.johnny.john.bakingapp.model.Ingredients;
import com.johnny.john.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int RECIPE_LOADER_ID = 1;
    private static String MOVIE_PATH = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipe;
    @BindView(R.id.grid_view)
    GridView recipeGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        recipeGridView.setEnabled(true);

        mAdapter = new RecipeAdapter(this, new ArrayList<Recipe>());
        recipeGridView.setAdapter(mAdapter);
        recipeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Recipe recipe = mRecipe.get(position);
                Ingredients ingredients = recipe.getIngredient().get(position);
                launchDetailActivity(recipe, ingredients);
                recipeGridView.setEnabled(false);

            }


        });


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(RECIPE_LOADER_ID, null, this);
        }

    }

    //launching detail Activity
    private void launchDetailActivity(Recipe recipe, Ingredients ingredients) {

        Intent intent = new Intent(this, DetailRecipeActivity.class);
        intent.putExtra(DetailRecipeActivity.RECIPE_ID, recipe);
        intent.putExtra(DetailRecipeActivity.INGREDIENTS_LIST, ingredients);


        startActivity(intent);
    }

    //loader
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle bundle) {


        //sending Recipe Json to the loader to be process in the background
        String RECIPE_REQUEST_URL = MOVIE_PATH;
        Uri baseUri = Uri.parse(RECIPE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new RecipeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        mAdapter.clear();
        mRecipe = recipes;


        if (recipes != null && !recipes.isEmpty()) {
            mAdapter.addAll(recipes);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.clear();

    }
}
