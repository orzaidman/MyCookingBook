package com.or.mycookingbook.data;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Account {

    private String userPhoneNumber;
    private ArrayList<Recipe> recipesMain;
    private ArrayList<Recipe> recipesFirsts;
    private ArrayList<Recipe> recipesDessert;
    private ArrayList<Recipe> recipesAdds;
    private String uuidAccount;


    public Account() {
        recipesMain = new ArrayList<Recipe>();
        recipesFirsts = new ArrayList<Recipe>();
        recipesDessert = new ArrayList<Recipe>();
        recipesAdds = new ArrayList<Recipe>();
    }




    public Account(String data) {
        this(createAccountFromString(data));
    }




    public Account(Account other) {
        this.recipesAdds = other.recipesAdds;
        this.recipesDessert = other.recipesDessert;
        this.recipesFirsts = other.recipesFirsts;
        this.recipesMain = other.recipesMain;
        this.userPhoneNumber = other.userPhoneNumber;
        this.uuidAccount = other.uuidAccount;
    }

    public static Account createAccountFromString(String data) {
        Account tempA;
        if (data == "NA") {
            tempA = new Account();
        }
        else {
            tempA = new Gson().fromJson(data, Account.class);
        }
        return tempA;
    }




    public Account(String userPhoneNumber, String uuidAccount) {
        this.uuidAccount = uuidAccount;
        this.userPhoneNumber = userPhoneNumber;

        recipesMain = new ArrayList<Recipe>();
        recipesFirsts = new ArrayList<Recipe>();
        recipesDessert = new ArrayList<Recipe>();
        recipesAdds = new ArrayList<Recipe>();


    }

    public Recipe getRecipeByNameAdds(String name){
        for (int i =0; i<this.recipesAdds.size();i++)
            if(this.recipesAdds.get(i).getName().equals(name))
                return recipesAdds.get(i);
    return null;
    }


    public Recipe getRecipeByNameDessert(String name){
        for (int i =0; i<this.recipesDessert.size();i++)
            if(this.recipesDessert.get(i).getName().equals(name))
                return recipesDessert.get(i);
        return null;
    }

    public Recipe getRecipeByNameFirsts(String name){
        for (int i =0; i<this.recipesFirsts.size();i++)
            if(this.recipesFirsts.get(i).getName().equals(name))
                return recipesFirsts.get(i);
        return null;
    }

    public Recipe getRecipeByNameMain(String name){
        for (int i =0; i<this.recipesMain.size();i++)
            if(this.recipesMain.get(i).getName().equals(name))
                return recipesMain.get(i);
        return null;
    }

    public ArrayList<Recipe> getRecipesMain() {
        return recipesMain;
    }

    public void setRecipesMain(ArrayList<Recipe> recipesMain) {
        this.recipesMain = recipesMain;
    }

    public ArrayList<Recipe> getRecipesFirsts() {
        return recipesFirsts;
    }

    public void setRecipesFirsts(ArrayList<Recipe> recipesFirsts) {
        this.recipesFirsts = recipesFirsts;
    }




    public void clearRecipesDessert()
    {
        this.recipesDessert.clear();
        recipesDessert = new ArrayList<Recipe>();
    }

    public void clearRecipesFirsts()
    {
        this.recipesFirsts.clear();
        recipesFirsts = new ArrayList<Recipe>();
    }

    public void clearRecipesAdds()
    {
        this.recipesAdds.clear();
        recipesAdds = new ArrayList<Recipe>();
    }

    public void clearRecipesMain()
    {
        this.recipesMain.clear();
        recipesMain = new ArrayList<Recipe>();
    }

    public String getUuidAccount() {
        return uuidAccount;
    }

    public void setUuidAccount(String uuidAccount) {
        this.uuidAccount = uuidAccount;
    }

    public void addToMain(Recipe add){
        recipesMain.add(add);
    }

    public void addToFirsts(Recipe add){
        recipesFirsts.add(add);
    }
    public void addToDessert(Recipe add){
        recipesDessert.add(add);
    }

    public void addToAdds(Recipe add){
        recipesAdds.add(add);
    }



    public ArrayList<Recipe> updateRecipesAdds(Recipe update,int pos){
        recipesAdds.set(pos,update);
        return recipesAdds;
    }

    public ArrayList<Recipe> updateRecipesMain(Recipe update,int pos){
        recipesMain.set(pos,update);
        return recipesMain;
    }

    public ArrayList<Recipe> updateRecipesFirsts(Recipe update, int pos){
        recipesFirsts.set(pos,update);
        return recipesFirsts;
    }

    public ArrayList<Recipe> updateRecipesDessert(Recipe update,int pos){
        recipesDessert.set(pos,update);
        return recipesDessert;
    }


    public ArrayList<Recipe> getRecipesDessert() {
        return recipesDessert;
    }

    public ArrayList<Recipe> getRecipesAdds() {
        return recipesAdds;
    }

    public void setRecipesAdds(ArrayList<Recipe> recipesAdds) {
        this.recipesAdds = recipesAdds;
    }

    public void setRecipesDessert(ArrayList<Recipe> recipesDessert) {
        this.recipesDessert = recipesDessert;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }




}
