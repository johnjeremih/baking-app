package com.johnny.john.bakingapp.activity;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Toast;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.fragments.FullStepsFragment;
import com.johnny.john.bakingapp.model.Steps;
import java.util.List;

import butterknife.ButterKnife;


public class StepDetailActivity extends AppCompatActivity {


    public static final String POSITION = "extra_position";
    public static final int DEFAULT_POSITION = -1;
    public static final String RECIPE_LIST = "extra_recipe";
    public static final String RECIPE_TITLE = "extra_title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final List<Steps> recipe1 = (List<Steps>) intent.getSerializableExtra(RECIPE_LIST);
        final int position = intent.getIntExtra(POSITION, DEFAULT_POSITION);
        String title = intent.getStringExtra(RECIPE_TITLE);

        final Steps recipe = recipe1.get(position);
        this.setTitle(title);



        FragmentManager fragmentManager = getSupportFragmentManager(); // the info is saved
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment fullStepsFragment = fragmentManager.findFragmentByTag(FullStepsFragment.class.getSimpleName());

        if(fullStepsFragment == null) {
            fullStepsFragment = FullStepsFragment.newInstanceDetail(recipe1);
            ((FullStepsFragment)fullStepsFragment).setStep(recipe1);
            ((FullStepsFragment)fullStepsFragment).setPosition(position);
        }

        ft.replace(R.id.stepsDetailFragment, fullStepsFragment, FullStepsFragment.class.getSimpleName()).commit();
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
        Toast.makeText(this, R.string.step_error, Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
