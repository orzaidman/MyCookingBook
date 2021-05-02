package com.or.mycookingbook.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.or.mycookingbook.R;
import com.or.mycookingbook.data.Account;
import com.or.mycookingbook.data.MainAdapterRecipes;
import com.or.mycookingbook.data.MySheredP;
import com.or.mycookingbook.data.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Recipes_Dessert extends AppCompatActivity {
    private static final String KEY_RECIPE = "Recipe";


    private ListView dessert_recipe;
    private MainAdapterRecipes adapter;
    private View view;
    private String uuid;
    private MySheredP msp;
    private Gson gson = new Gson();
    public static final String KEY_Account = "account";
    private Account account;
    private Recipe chosenRecipe;
    private List listNew = new ArrayList();
    private Button back,add;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipes_dessert);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        msp = new MySheredP(this);

        findViews(view);

        getFromMSP();

        dessert_recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = listNew.get(position).toString();

                chosenRecipe = account.getRecipeByNameDessert(tempName);
                openNewActivityReadRecipe();
            }


        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openNewActivityNewRecipe();
        }
    });
    }

    private void openNewActivityNewRecipe() {
        Intent intent = new Intent(this, newRecipe.class);
        startActivity(intent);
        finish();
    }
    public void findViews(View view) {
        dessert_recipe = findViewById(R.id.dessert_recipe);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);

    }

    @Override
    public void onBackPressed() {
        openNewActivityMain();
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
    }


    private void openNewActivityReadRecipe() {
        Intent intent = new Intent(this, ReadRecipeDessert.class);
        String s = new Gson().toJson(chosenRecipe);
        intent.putExtra(KEY_RECIPE, s);
        startActivity(intent);
        finish();
    }



    private void putOnMSP() {

        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getFromMSP() {

        String data = msp.getString(KEY_Account, "NA");
        account = new Account(data);

        for (int i = 0; i < account.getRecipesDessert().size(); i++) {
            listNew.add(account.getRecipesDessert().get(i).getName());
        }
        listNew = checkDoubles();

        adapter = new MainAdapterRecipes(this, listNew, uuid);
        dessert_recipe.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List checkDoubles() {

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(listNew);
        listNew.clear();
        listNew.addAll(hashSet);
        return   listNew;
    }

}