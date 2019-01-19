package com.johnny.john.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.model.Ingredients;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsAdapter extends ArrayAdapter<Ingredients> {


    @BindView(R.id.ingredient_tv) TextView ingredient;
    @BindView(R.id.quantity_tv) TextView quantity;
    @BindView(R.id.measure_tv) TextView measure;

    public IngredientsAdapter(Context context, List<Ingredients> ingredients) {
        super(context, 0, ingredients);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridIngredientView = convertView;

        if (gridIngredientView == null) {
            gridIngredientView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ingredients_item, parent, false);
        }

        Ingredients currentIngredient = getItem(position);
        ButterKnife.bind(this,gridIngredientView);

        ingredient.setText(currentIngredient.getIngredient());
        quantity.setText(currentIngredient.getQuantity());
        measure.setText(currentIngredient.getMeasure());


        return gridIngredientView;
    }


}
