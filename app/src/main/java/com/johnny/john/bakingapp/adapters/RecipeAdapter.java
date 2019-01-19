package com.johnny.john.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends ArrayAdapter<Recipe> {

    @BindView(R.id.recipe_name)
    TextView recipeName;
    @BindView(R.id.servings_tv)
    TextView servings;
    @BindView(R.id.recipe_image)
    ImageView imageView;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;

        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.card_view, parent, false);

        }

        Recipe currentRecipe = getItem(position);

        String name = currentRecipe.getName();
        ButterKnife.bind(this,gridItemView);
        recipeName.setText(name);
        servings.setText(currentRecipe.getServings());
        ImageSetter(name);

        return gridItemView;
    }

    //setting a pre-set image to the recipes
    private void ImageSetter(String name) {
        switch (name){
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


}
