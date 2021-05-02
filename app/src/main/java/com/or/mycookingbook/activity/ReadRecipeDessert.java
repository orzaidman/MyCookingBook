package com.or.mycookingbook.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.or.mycookingbook.R;
import com.or.mycookingbook.data.Account;
import com.or.mycookingbook.data.MySheredP;
import com.or.mycookingbook.data.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ReadRecipeDessert extends AppCompatActivity {
    private static final String KEY_RECIPE = "Recipe";
    public static final String KEY_Account = "account";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private ListView read_LIST_all_ingredints;
    private TextView read_TXT_lavels, read_title;
    private View view;
    private String s;
    private Recipe recipe;
    private List listNew = new ArrayList();
    private ArrayAdapter arrayAdapter;

    private Button list_remove, list_edit, list_share,back;
    private MySheredP msp;
    private Gson gson = new Gson();
    private Account account;
    private ArrayList<Recipe> allRecipesDeesert = new ArrayList<>();
    private int recipeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read_recipe_dessert);
        msp = new MySheredP(this);

        findViews(view);
        s = getIntent().getStringExtra(ReadRecipeDessert.KEY_RECIPE);

        recipe = new Gson().fromJson(s, Recipe.class);
        read_TXT_lavels.setText(recipe.getPreparation());
        read_title.setText(recipe.getName());



        readIngredient();
        getFromMSP();

        recipeIndex = getIndex(recipe);
        list_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRecipe();
            }
        });

        list_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRecipe();
            }
        });

        list_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityDesserts();
            }
        });
    }


    private void editRecipe(){
        final List listIngridents = new ArrayList();
        final ArrayAdapter arrayAdapterIngridents;

        final String recipeName = recipe.getName();


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.popup_update, null);
        EditText name = view.findViewById(R.id.popup_TXT_name);
        ListView ingredient = view.findViewById(R.id.listView_ingredient);
        Button addIngradient =  view.findViewById(R.id.add);
        final EditText popup_TXT_name = view.findViewById(R.id.popup_TXT_name);
        final EditText popup_TXT_title_ingredient = view.findViewById(R.id.popup_TXT_title_ingredient);
        final EditText popupTXT_preparation_method = view.findViewById(R.id.popupTXT_preparation_method);
        final Button popup_BTN_yes = view.findViewById(R.id.popup_BTN_yes);
        final Button popup_BTN_no = view.findViewById(R.id.popup_BTN_no);
        final ListView listView_ingredient = view.findViewById(R.id.listView_ingredient);

        for(int i=0;i< recipe.getIngredient().size(); i++)
            listIngridents.add( " "+recipe.getIngredient().get(i));

        arrayAdapterIngridents = new ArrayAdapter(this, R.layout.mylist_new, listIngridents);
        popup_TXT_name.setText(recipe.getName());
        popupTXT_preparation_method.setText(recipe.getPreparation());
        listView_ingredient.setAdapter(arrayAdapterIngridents);
        mBuilder.setView(view);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

        listView_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ReadRecipeDessert.this).create();
                alertDialog.setTitle("עריכה");

                final EditText input = new EditText(ReadRecipeDessert.this);
                String all = listIngridents.get(position).toString();


                input.setText(all);
                alertDialog.setView(input);

                alertDialog.setMessage(" האם ברצונך לערוך את המצרך? ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "שמור",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String newNameStr = input.getText().toString();
                                if (newNameStr.length() > 0) {
                                    listIngridents.set(position," "+ newNameStr);
                                }
                                else
                                    listIngridents.remove(position);

                                arrayAdapterIngridents.notifyDataSetChanged();
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

                                listIngridents.remove(position);
                                arrayAdapterIngridents.notifyDataSetChanged();
                                dialog.dismiss();                            }
                        });

                alertDialog.show();

            }
        });

        addIngradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listIngridents.add(" "+popup_TXT_title_ingredient.getText());

                arrayAdapterIngridents.notifyDataSetChanged();
                popup_TXT_title_ingredient.setText("");
            }
        });

        popup_BTN_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setName(popup_TXT_name.getText().toString());
                recipe.setPreparation(popupTXT_preparation_method.getText().toString());
                recipe.setIngredient((ArrayList)listIngridents);
                account.updateRecipesDessert(recipe,recipeIndex);
       //         myRef.child("Users").child(account.getUserPhoneNumber()).child("Dessert").setValue(account.getRecipesDessert());
                myRef.child("Users").child(account.getUserPhoneNumber()).setValue(account);
                putOnMSP();
                getFromMSP();
                openNewActivityReadRecipe();
                alertDialog.hide();
            }
        });

        popup_BTN_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });

    }



    private void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, print(recipe));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private String print(Recipe recipe){
        return recipe.getName() + ":\n" + recipe.toString();
    }
    private void removeRecipe() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("מחיקה");

        alertDialog.setMessage(" האם ברצונך למחוק את המתכון? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        account.getRecipesDessert().remove(recipeIndex);
                        //        account.setRecipesDesserts(allRecipesDesserts);
                     //   myRef.child("Users").child(account.getUserPhoneNumber()).child("Desserts").setValue( account.getRecipesDessert());
                        myRef.child("Users").child(account.getUserPhoneNumber()).setValue( account);
                        putOnMSP();
                        // getFromMSP();
                        openActivityDesserts();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ביטול",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void openActivityDesserts() {
        Intent intent = new Intent(this, Recipes_Dessert.class);
        startActivity(intent);
        finish();
    }

    private void readIngredient() {

        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.mylist_new, listNew);
        read_LIST_all_ingredints.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


    }

    private void findViews(View view) {
        read_LIST_all_ingredints = findViewById(R.id.read_LIST_all_ingredints);
        read_TXT_lavels = findViewById(R.id.read_TXT_lavels);
        read_title = findViewById(R.id.read_title);
        list_remove = findViewById(R.id.list_remove);
        list_edit = findViewById(R.id.list_edit);
        list_share = findViewById(R.id.list_share);
        back = findViewById(R.id.back);

    }

    private void getFromMSP() {

        String data = msp.getString(KEY_Account, "NA");

        account = new Account(data);


        for (int i = 0; i < recipe.getIngredient().size(); i++) {
            listNew.add(" "+recipe.getIngredient().get(i));
        }


        arrayAdapter.notifyDataSetChanged();

    }

    private void putOnMSP() {

        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }




    private int getIndex(Recipe recipe) {

        for (int i = 0; i < account.getRecipesDessert().size(); i++) {
            if (account.getRecipesDessert().get(i).getName().equals(recipe.getName()))
                return i;

        }
        return -1;
    }

    private void openNewActivityReadRecipe() {
        Intent intent = new Intent(this, ReadRecipeDessert.class);
        String s = new Gson().toJson(recipe);
        intent.putExtra(KEY_RECIPE, s);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openActivityDesserts();
    }
}