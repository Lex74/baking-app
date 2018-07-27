package com.lexoid.bakingapp.ui.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{
    private ArrayList<Step> steps;
    private StepClickListener stepClickListener;

    public StepsAdapter(ArrayList<Step> steps, StepClickListener stepClickListener) {
        super();
        this.steps = steps;
        this.stepClickListener = stepClickListener;
    }

    @NonNull
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder{
        private View itemView;

        @BindView(R.id.step_iv)
        ImageView stepImage;
        @BindView(R.id.description_tv)
        TextView stepDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            ButterKnife.bind(this, itemView);
        }

        void bind(final Step step){
            String thumbnailURL = step.getThumbnailURL();
            if (!TextUtils.isEmpty(thumbnailURL)) {
                Picasso.get()
                        .load(thumbnailURL)
                        .placeholder(R.drawable.ic_movies)
                        .into(stepImage);
            }

            if (step.getId() == 0){
                stepDescription.setText(step.getShortDescription());
            } else {
                stepDescription.setText(String.format("%s. %s", step.getId(), step.getShortDescription()));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stepClickListener.onStepClick(steps, getAdapterPosition());
                    itemView.setFocusable(true);
                    itemView.requestFocus();
                }
            });
        }
    }

    public interface StepClickListener{
        void onStepClick(ArrayList<Step> steps, int stepId);
    }
}

