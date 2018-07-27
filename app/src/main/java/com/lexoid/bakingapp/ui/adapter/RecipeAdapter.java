package com.lexoid.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    ItemClickListener itemClickListener;

    public RecipeAdapter(List<Recipe> recipes, ItemClickListener itemClickListener){
        this.recipes = recipes;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bindRecipe(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_image)
        ImageView recipeIv;

        @BindView(R.id.recipe_name)
        TextView recipeNameTv;

        private View itemView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ButterKnife.bind(this, itemView);
        }

        public void bindRecipe(final Recipe recipe){
            String recipeName = recipe.getName();
            String recipeImage = recipe.getImage();

            recipeNameTv.setText(recipeName);

            if (!TextUtils.isEmpty(recipeImage)) {
                Picasso.get()
                        .load(recipeImage)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(recipeIv);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(recipe);
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick (Recipe model);
    }
}
