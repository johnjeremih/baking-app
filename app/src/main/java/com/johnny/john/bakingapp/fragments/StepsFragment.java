package com.johnny.john.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.adapters.StepsAdapter;
import com.johnny.john.bakingapp.model.Steps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    public static String STEPS_ARGS_KEY ="key";
    public List<Steps> steps;
    private Context context;
    private StepsAdapter.StepAdapterClickHandler clickHandler;
    public int position;
    OnIngredientsClickListener mCallback;
    @BindView(R.id.recycler_steps)
    RecyclerView mRecycleView;
    @BindView(R.id.ingredients_button)
    TextView ingredientButton;


    public void setPosition(int position) {
        this.position = position;
    }

    public interface OnIngredientsClickListener {
        void onClickHandlerIngredients();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        try {
            mCallback = (OnIngredientsClickListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnIngredientsClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        // retrieving the steps
        assert args != null;
        if(args.containsKey(STEPS_ARGS_KEY)) {

            // getting the json str
            String stepsJson = args.getString(STEPS_ARGS_KEY);

            // specify the type
            Type stepsListType = new TypeToken<List<Steps>>() {}.getType();

            // convert back to List
            this.steps = new Gson().fromJson(stepsJson, stepsListType);
        }
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_recipe_fragment, container, false);
        ButterKnife.bind(this,view);

        StepsAdapter StepsAdapter = new StepsAdapter(getContext(),steps,clickHandler,position);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(StepsAdapter);

        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickHandlerIngredients();
            }
        });

        return view;
    }

    public void setStep(List<Steps> recipe1) {
        steps = recipe1;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setClickHandler(StepsAdapter.StepAdapterClickHandler handler){
        clickHandler = handler;

    }




    // Helper function
    public static StepsFragment newInstance(List<Steps> steps) {
        // We create the StepsFragment instance
        StepsFragment fragment = new StepsFragment();

        // We create a bundle
        Bundle args = new Bundle();

        // Created a json string from the steps list
        String strSteps = new Gson().toJson(steps);
        args.putString(STEPS_ARGS_KEY, strSteps);

        fragment.setArguments(args);

        return fragment;
    }


}
