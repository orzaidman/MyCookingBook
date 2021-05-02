package com.or.mycookingbook.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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



public class newRecipe extends AppCompatActivity {
     private Spinner type;
     private EditText new_EDIT_name;
     private EditText new_EDIT_ingredient,popupTXT_preparation_method;
   private Button new_BTN_add, new_add_one_ingr,back;
   private Recipe newRecipe;
   private ListView listView_ingredient;
   private ArrayList<String> stringArrayList;
    ArrayAdapter<String> stringArrayAdapter;
    private View view;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private ArrayList<Account> allAccounts = new ArrayList<>();
    private com.or.mycookingbook.data.allAccounts tempAllAccounts;
    private  Account account = new Account();
    private String uuid;

    private MySheredP msp;
    private Gson gson = new Gson();
    public static final String KEY_Account = "account";
    private InterstitialAd mInterstitialAd;

    public newRecipe() {
        tempAllAccounts = new allAccounts();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_recipe);
        msp = new MySheredP(this);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        findViews(view);

        readFromDB();

        stringArrayList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,stringArrayList);


        listView_ingredient.setAdapter(stringArrayAdapter);
        new_add_one_ingr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView_ingredient.setCacheColorHint(Color.WHITE);
                stringArrayList.add(new_EDIT_ingredient.getText().toString());
                stringArrayAdapter.notifyDataSetChanged();
                new_EDIT_ingredient.getText().clear();
            }
        });

        listView_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(newRecipe.this).create();
                alertDialog.setTitle("עריכה");

                final EditText input = new EditText(newRecipe.this);
                String all = stringArrayList.get(position);


                input.setText(all);
                alertDialog.setView(input);

                alertDialog.setMessage(" האם ברצונך לערוך את המצרך? ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "שמור",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String newNameStr = input.getText().toString();
                                if (newNameStr.length() > 0) {
                                    stringArrayList.set(position, newNameStr);
                                }
                                else
                                    stringArrayList.remove(position);

                                stringArrayAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ביטול",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "מחיקה",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                stringArrayList.remove(position);
                                stringArrayAdapter.notifyDataSetChanged();
                                dialog.dismiss();                            }
                        });

                alertDialog.show();

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.type,R.layout.sppiner_style);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new_BTN_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(checkNullName()==0) {
                    buildRecipe();
                    openNewActivityMain();
                  //  showInterstitial();
                }

                else if(checkNullName()==1)
                    new_EDIT_name.setError("שדה חובה");

                else {
                    new_EDIT_name.setError(" כותרת קיימת ");

                    }
                }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();

        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();

        }
    }

    private void readFromDB() {
        // Read from the database
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Account tempAccount = ds.getValue(Account.class);
                        allAccounts.add(tempAccount);
                    }
                    tempAllAccounts.setAllAccounts(allAccounts);
                    account = tempAllAccounts.getAccountByUUid(uuid);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("or", "Failed to read value.", error.toException());
            }
        });

    }

    private void findViews(View view) {
        type = findViewById(R.id.type) ;
        new_EDIT_name = findViewById(R.id.new_EDIT_name);
        new_BTN_add = findViewById(R.id.new_BTN_add);
        new_add_one_ingr = findViewById(R.id.new_add_one_ingr);
        listView_ingredient = findViewById(R.id.listView_ingredient);
        new_EDIT_ingredient = findViewById(R.id.new_EDIT_ingredient);
        popupTXT_preparation_method = findViewById(R.id.popupTXT_preparation_method);
        back = findViewById(R.id.back);
    }



    private void buildRecipe() {
        int typeNew = (int) type.getSelectedItemId();
        newRecipe = new Recipe(new_EDIT_name.getText().toString(), typeNew, stringArrayList, popupTXT_preparation_method.getText().toString());
        if (typeNew == 3)
            account.addToDessert(newRecipe);

        if (typeNew == 1)
            account.addToMain(newRecipe);

        if (typeNew == 0)
            account.addToFirsts(newRecipe);


        if (typeNew == 2)
            account.addToAdds(newRecipe);

        myRef.child("Users").child(account.getUserPhoneNumber()).setValue(account);

        putOnMSP();
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

    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }

    private int checkNullName(){
        if(new_EDIT_name.getText().length() ==0)
            return 1;
        String typeSelected = type.getSelectedItem().toString();
        if(typeSelected.equals("ראשונות"))
          for (int i =0; i< account.getRecipesFirsts().size();i++)
          {
              if(account.getRecipesFirsts().get(i).getName().equals(new_EDIT_name.getText().toString()))
                  return 2;
          }
        if(typeSelected.equals("עיקריות"))
            for (int i =0; i< account.getRecipesMain().size();i++)
            {
                if(account.getRecipesMain().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }

        if(typeSelected.equals("תוספות"))
            for (int i =0; i< account.getRecipesAdds().size();i++)
            {
                if(account.getRecipesAdds().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }


        if(typeSelected.equals("קינוחים"))
            for (int i =0; i< account.getRecipesDessert().size();i++)
            {
                if(account.getRecipesDessert().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }

        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_ads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

    /*    if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
                openNewActivityMain();

            }
        });
        return interstitialAd;
    }



    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }


}
