package com.or.mycookingbook.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private String name;
    private int type;
    private ArrayList<String> ingredient = new ArrayList<>();
    private String preparation;



    public Recipe(String name, int type, ArrayList<String> ingredient, String preparation) {
        this.name = name;
        this.type = type;
        this.ingredient = ingredient;
        this.preparation = preparation;
    }

    public Recipe(Recipe other) {
        this.name = other.name;
        this.type = other.type;
        this.ingredient = other.ingredient;
        this.preparation = other.preparation;

    }
    public Recipe() {
    }

    public String  RecipesToString(Recipe temp){
        return temp.getName()+": \n" + temp.toString();

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<String> ingredient) {
        this.ingredient = ingredient;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name);
    }




    @NonNull
    @Override
    public String toString() {

        String s = "מצרכים: \n";
        for (int i = 0; i< ingredient.size();i++)
        {
            s+=ingredient.get(i) +"\n";

        }
        s+="\nשלבי הכנה:\n"+ this.preparation;

        return s;
    }
}
