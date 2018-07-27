package com.lexoid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.models.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepDetailActivity extends AppCompatActivity {

    private ArrayList<Step> steps;
    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        steps = getIntent().getParcelableArrayListExtra(StepDetailFragment.ARG_STEPS);
        int stepId = getIntent().getIntExtra(StepDetailFragment.ARG_STEP_ID, -1);
        step = steps.get(stepId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(step.getShortDescription());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            openStepInFragment(step);
        }
    }

    private void openStepInFragment(Step step) {
        StepDetailFragment fragment = StepDetailFragment.getFragment(step);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.next_button)
    public void showNextStep(){
        int stepPosition = steps.indexOf(step) + 1;
        if (stepPosition < steps.size()) {
            step = steps.get(stepPosition);
            openStepInFragment(step);
        }
    }

    @OnClick(R.id.prev_button)
    public void showPreviousStep(){
        int stepPosition = steps.indexOf(step) - 1;
        if (stepPosition >= 0) {
            step = steps.get(stepPosition);
            openStepInFragment(step);
        }
    }


}
