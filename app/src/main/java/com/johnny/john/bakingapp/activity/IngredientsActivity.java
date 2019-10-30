package com.johnny.john.bakingapp.activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.adapters.IngredientsAdapter;
import com.johnny.john.bakingapp.data.Contractor.IngredientsEntry;
import com.johnny.john.bakingapp.model.Ingredients;
import com.johnny.john.bakingapp.model.Recipe;
import com.johnny.john.bakingapp.widget.RecipeWidgetProvider;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {


    private IngredientsAdapter mAdapter;
    public static final String RECIPE_LIST = "extra_list_recipe";
    public static final String TITLE = "extra_title";
    public static String INGREDIENTS_LIST = "extra_list_ingredients";
    @BindView(R.id.ingredients_grid_view)
    GridView ingredientsGridView;
    @BindView(R.id.image_recipe)
    ImageView imageView;
    @BindView(R.id.widget_list_icon)
    ImageView widgetListIcon;
    @BindView(R.id.widget_click_handler)
    RelativeLayout clickHandler;
    private boolean isAdded = false;

    private String mTitle;
    private Context mContext;
    private Recipe mRecipe;

    private String LIST_STATE_RECIPE = "extra_state_list_recipe";
    private String LIST_STATE_INGREDIENTS = "extra_state_list_ingredient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        mContext = this;
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mRecipe = (Recipe) Objects.requireNonNull(intent).getSerializableExtra(RECIPE_LIST);
        mTitle = intent.getStringExtra(TITLE);

        this.setTitle(mTitle);
        ImageSetter(mTitle);

        mAdapter = new IngredientsAdapter(this, mRecipe.getIngredient());
        ingredientsGridView.setAdapter(mAdapter);

        setFavorites();

        clickHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAdded) {

                    removeList();

                } else {

                   insertList();
                }

            }
        });


    }

    // setting favorite
    private void setFavorites() {
        Uri queryUri = IngredientsEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf( mRecipe.getId())).build();
        Cursor cursor = getContentResolver().query(
                queryUri,
                null,
                null,
                null,
                null);

        if (cursor == null) {
            return;
        }


        if (cursor.moveToFirst()) {
            widgetListIcon.setImageResource(R.drawable.check_list_marked);
            isAdded = true;
            cursor.close();
        }


    }

    //adding Recipe to the favorite's list
    private void insertList() {

        ContentValues values = new ContentValues();
        values.put(IngredientsEntry.COLUMN_RECIPE_ID, mRecipe.getId());
        values.put(IngredientsEntry.COLUMN_RECIPE_NAME, mTitle);


        for (Ingredients ingredients : mRecipe.getIngredient()){

            values.put(IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT, ingredients.getIngredient());
            values.put(IngredientsEntry.COLUMN_INGREDIENT_MEASURE, ingredients.getMeasure());
            values.put(IngredientsEntry.COLUMN_INGREDIENT_QUANTITY, ingredients.getQuantity());


            Uri mUri = getContentResolver().insert(IngredientsEntry.CONTENT_URI, values);


            if (mUri != null) {
                Toast.makeText(IngredientsActivity.this, mTitle + " " + getString(R.string.list_widget_added), Toast.LENGTH_LONG).show();
                widgetListIcon.setImageResource(R.drawable.check_list_marked);
                isAdded = true;
                RecipeWidgetProvider.sendRefreshBroadcast(mContext);


            }

        }


    }

    //removing Recipe from the favorite's list
    private void removeList() {
        Uri deleteUri = IngredientsEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf( mRecipe.getId())).build();
        int delete = getContentResolver().delete(deleteUri, null, null);

        if (delete != 0) {

            Toast.makeText(IngredientsActivity.this, mTitle + " " + getString(R.string.list_widget_removed), Toast.LENGTH_LONG).show();
            widgetListIcon.setImageResource(R.drawable.check_list_unmarked);
            isAdded = false;
            RecipeWidgetProvider.sendRefreshBroadcast(mContext);

        }


    }

    //adding a pre-set photos
    private void ImageSetter(String title) {
        switch (title) {
            case "Nutella Pie":
                imageView.setImageResource(R.drawable.nutella_pie);
                break;

            case "Brownies":
                imageView.setImageResource(R.drawable.brownies);
                break;
            case "Yellow Cake":
                imageView.setImageResource(R.drawable.yellow_cake);
                break;
            case "Cheesecake":
                imageView.setImageResource(R.drawable.cheesecake);
                break;

            default:
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
