package com.or.mycookingbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.or.mycookingbook.R;

public class MainActivity extends AppCompatActivity {
    private Button main_BTN_new_recipe, main_BTN_view_all_recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        main_BTN_view_all_recipe = findViewById(R.id.main_BTN_view_all_recipe);
        main_BTN_new_recipe = findViewById(R.id.main_BTN_new_recipe);




        main_BTN_new_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityNewRecipe();
            }
        });

        main_BTN_view_all_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityAllRecipes();
            }
        });
    }

    private void openNewActivityAllRecipes() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityNewRecipe() {
        Intent intent = new Intent(this, newRecipe.class);
        startActivity(intent);
        finish();
    }
}
