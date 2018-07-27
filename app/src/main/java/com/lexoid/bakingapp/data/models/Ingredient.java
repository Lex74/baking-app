package com.lexoid.bakingapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private double quantity;
    private String measure;
    private String ingredient;

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getQuantity() { return this.quantity; }

    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getMeasure() { return this.measure; }

    public void setMeasure(String measure) { this.measure = measure; }

    public String getIngredient() { return this.ingredient; }

    public void setIngredient(String ingredient) { this.ingredient = ingredient; }
}
