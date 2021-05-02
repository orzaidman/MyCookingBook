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

public class Recipes_Firsts extends AppCompatActivity {
    private static final String KEY_RECIPE = "Recipe";
    public static final String KEY_Account = "account";

    private ListView first_all;
    private View view;
    private String uuid;
    private MySheredP msp;
    private Gson gson = new Gson();
    private Account account;
    private Recipe chosenRecipe;
    private MainAdapterRecipes adapter;
    private List listNew = new ArrayList();
    private Button back, add;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipes_first);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        msp = new MySheredP(this);

        findViews(view);

        adapter = new MainAdapterRecipes(this, listNew, uuid);
        first_all.setAdapter(adapter);


        getFromMSP();

        first_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = listNew.get(position).toString();

                chosenRecipe = account.getRecipeByNameFirsts(tempName);
                openNewActivityReadRecipe();
            }


        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityNewRecipe();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
    }

    public void findViews(View view) {
        first_all = findViewById(R.id.first_all);
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

    private void openNewActivityNewRecipe() {
        Intent intent = new Intent(this, newRecipe.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityReadRecipe() {
        Intent intent = new Intent(this, ReadRecipeFirsts.class);
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

        for (int i = 0; i < account.getRecipesFirsts().size(); i++) {
            listNew.add(account.getRecipesFirsts().get(i).getName());
        }
        listNew = checkDoubles();

        adapter = new MainAdapterRecipes(this, listNew, uuid);
        first_all.setAdapter(adapter);
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
