package com.or.mycookingbook.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

public class Recipes_Main extends AppCompatActivity {
    private static final String KEY_RECIPE = "Recipe";

    private ListView main_all;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipes_main);
        msp = new MySheredP(this);


        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        findViews(view);

        getFromMSP();

        main_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = listNew.get(position).toString();

                chosenRecipe = account.getRecipeByNameMain(tempName);
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
        main_all = findViewById(R.id.main_all);
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
        Intent intent = new Intent(this, ReadRecipeMain.class);
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

        for (int i = 0; i < account.getRecipesMain().size(); i++) {
            listNew.add(account.getRecipesMain().get(i).getName());
        }
        listNew = checkDoubles();

        adapter = new MainAdapterRecipes(this, listNew, uuid);
        main_all.setAdapter(adapter);
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
