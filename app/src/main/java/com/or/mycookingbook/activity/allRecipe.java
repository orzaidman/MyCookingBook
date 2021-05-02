package com.or.mycookingbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.or.mycookingbook.R;
import com.or.mycookingbook.data.Account;
import com.or.mycookingbook.data.MySheredP;
import com.or.mycookingbook.data.Recipe;
import com.or.mycookingbook.data.allAccounts;

import java.util.ArrayList;


public class allRecipe extends AppCompatActivity {

    public static final String KEY_Account = "account";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    public String uuid;
    private ArrayList<Account> allAccounts = new ArrayList<>();
    private com.or.mycookingbook.data.allAccounts tempAllAccounts = new allAccounts();
    private Account account = new Account();
    private MySheredP msp;
    private Gson gson = new Gson();
    private Button all_BTN_first,all_BTN_adds,all_BTN_main,all_BTN_dessert,back;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_recipe);

        findViews(view);
   //     ListView listView = (ListView) findViewById(R.id.listView_ingredient);

        msp = new MySheredP(this);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        initListData();
        all_BTN_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityDessert();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });

        all_BTN_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityFirsts();
            }
        });

        all_BTN_adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityAdds();
            }
        });

        all_BTN_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityRecipesMain();
            }
        });


    }

    private void findViews(View view) {
        all_BTN_first = findViewById(R.id.all_BTN_first);
        all_BTN_adds = findViewById(R.id.all_BTN_adds);
        all_BTN_main = findViewById(R.id.all_BTN_main);
        all_BTN_dessert = findViewById(R.id.all_BTN_dessert);
        back = findViewById(R.id.back);

    }

    private void openNewActivityFirsts() {
        Intent intent = new Intent(this, Recipes_Firsts.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityAdds() {
        Intent intent = new Intent(this, Recipes_Adds.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityRecipesMain() {
        Intent intent = new Intent(this, Recipes_Main.class);
        startActivity(intent);
        finish();
    }

    private void openNewActivityDessert() {
        Intent intent = new Intent(this, Recipes_Dessert.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openNewActivityMain();
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void initListData() {

        // Read from the database

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Account tempAccount = ds.getValue(Account.class);
                        allAccounts.add(tempAccount);

                    }
                    tempAllAccounts.setAllAccounts(allAccounts);
                    account = tempAllAccounts.getAccountByUUid(uuid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    public void readRecipeFirsts() {
        myRef.child("Users").child(account.getUserPhoneNumber()).child("Firsts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Recipe tempRecipe = ds.getValue(Recipe.class);
                        account.addToFirsts(tempRecipe);

                    }

                    putOnMSP();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void readRecipeAdds() {
        myRef.child("Users").child(account.getUserPhoneNumber()).child("Adds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Recipe tempRecipe = ds.getValue(Recipe.class);
                        account.addToAdds(tempRecipe);

                    }

                    putOnMSP();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void readRecipeDesert() {
        myRef.child("Users").child(account.getUserPhoneNumber()).child("Dessert").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Recipe tempRecipe = ds.getValue(Recipe.class);
                        account.addToDessert(tempRecipe);

                    }

                   putOnMSP();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void readRecipeMain() {
        myRef.child("Users").child(account.getUserPhoneNumber()).child("Main").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Recipe tempRecipe = ds.getValue(Recipe.class);
                        account.addToMain(tempRecipe);

                    }

                    putOnMSP();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }


}



