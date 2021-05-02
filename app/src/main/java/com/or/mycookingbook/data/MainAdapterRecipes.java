package com.or.mycookingbook.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.or.mycookingbook.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterRecipes extends BaseAdapter {


    Context context;
    List<String> listGroup = new ArrayList<>();;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private ArrayList<Account> allAccounts = new ArrayList<>();
    private Account account = new Account();
    private ArrayList<Recipe> allRecipesAdds = new ArrayList<>();
    private String uuid;
    private MySheredP msp;
    private Gson gson = new Gson();
    public static final String KEY_Account = "account";



    public MainAdapterRecipes(Context context, List<String> listGroup, String uuid) {
        this.context = context;
        this.listGroup = listGroup;
        this.uuid = uuid;
  //      cleanData();
      //  getFromMSP();
    }

    @Override
    public int getCount() {
        return listGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String group = (String) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textView = convertView.findViewById(R.id.list_parent);
        textView.setText(group);
        return convertView;
    }

//    @Override
//    public int getGroupCount() {
//        return listGroup.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return this.listItem.get(this.listGroup.get(groupPosition)).size();
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return this.listGroup.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//        String group = (String) getGroup(groupPosition);
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.list_group, null);
//        }
//        TextView textView = convertView.findViewById((R.id.list_parent));
//        textView.setText(group);
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
//        final String child = (String) getChild(groupPosition, childPosition);
//
//        final Recipe tempRecipe = new Gson().fromJson(child, Recipe.class);
//
//
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.list_item, null);
//        }
//
//        final TextView textView = convertView.findViewById(R.id.list_child);
//        Button list_remove = convertView.findViewById(R.id.list_remove);
//        Button list_edit = convertView.findViewById(R.id.list_edit);
//        Button list_share = convertView.findViewById(R.id.list_share);
//        textView.setText(tempRecipe.toString());
//
//        list_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final List listIngridents = new ArrayList();
//                final ArrayAdapter arrayAdapterIngridents;
//
//                final String recipeName = getGroup(groupPosition).toString();
//                final Recipe recipeToEdit = getRecipeByName(recipeName);
//
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                final View view = inflater.inflate(R.layout.popup_update, null);
//                EditText name = view.findViewById(R.id.popup_TXT_name);
//                ListView ingredient = view.findViewById(R.id.listView_ingredient);
//                Button addIngradient =  view.findViewById(R.id.add);
//                final EditText popup_TXT_name = view.findViewById(R.id.popup_TXT_name);
//                final EditText popup_TXT_title_ingredient = view.findViewById(R.id.popup_TXT_title_ingredient);
//                final EditText popupTXT_preparation_method = view.findViewById(R.id.popupTXT_preparation_method);
//                final Button popup_BTN_yes = view.findViewById(R.id.popup_BTN_yes);
//                final Button popup_BTN_no = view.findViewById(R.id.popup_BTN_no);
//                final ListView listView_ingredient = view.findViewById(R.id.listView_ingredient);
//
//                for(int i=0;i< tempRecipe.getIngredient().size(); i++)
//                    listIngridents.add( tempRecipe.getIngredient().get(i));
//                arrayAdapterIngridents = new ArrayAdapter(context, R.layout.mylist, listIngridents);
//
//                popup_TXT_name.setText(tempRecipe.getName());
//                popupTXT_preparation_method.setText(tempRecipe.getPreparation());
//                listView_ingredient.setAdapter(arrayAdapterIngridents);
//                mBuilder.setView(view);
//                final AlertDialog alertDialog = mBuilder.create();
//                alertDialog.show();
//
//                addIngradient.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        listIngridents.add(listIngridents.size()+1+". "+popup_TXT_title_ingredient.getText());
//
//                        arrayAdapterIngridents.notifyDataSetChanged();
//                        popup_TXT_title_ingredient.setText("");
//                    }
//                });
//
//                popup_BTN_yes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        recipeToEdit.setName(popup_TXT_name.getText().toString());
//                        recipeToEdit.setPreparation(popupTXT_preparation_method.getText().toString());
//                        recipeToEdit.setIngredient((ArrayList)listIngridents);
//                        account.updateRecipesAdds(recipeToEdit,groupPosition);
//                        myRef.child("Users").child(account.getUserPhoneNumber()).child("Adds").setValue(allRecipesAdds);
//                        putOnMSP();
//                        getFromMSP();
//                        notifyDataSetChanged();
//                        alertDialog.hide();
//                    }
//                });
//
//                popup_BTN_no.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.hide();
//                    }
//                });
//            }
//        });
//
//        list_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context.getApplicationContext(), "remove", Toast.LENGTH_LONG).show();
//                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                alertDialog.setTitle("מחיקה");
//
//                alertDialog.setMessage(" האם ברצונך למחוק את המתכון? ");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context.getApplicationContext(), "אישור", Toast.LENGTH_LONG).show();
//                                allRecipesAdds.remove(childPosition);
//                                account.setRecipesAdds(allRecipesAdds);
//                                myRef.child("Users").child(account.getUserPhoneNumber()).child("Adds").setValue(allRecipesAdds);
//                                putOnMSP();
//                                getFromMSP();
//                                listGroup.remove(groupPosition);
//                                notifyDataSetChanged();
//
//                            }
//                        });
//
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ביטול",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                alertDialog.show();
//            }
//        });
//        list_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, tempRecipe.toString());
//                sendIntent.setType("text/plain");
//                context.startActivity(sendIntent);
//            }
//        });
//
//
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//
//
//    public Recipe getRecipeByName(String name) {
//        for (int i = 0; i < allRecipesAdds.size(); i++) {
//            if (allRecipesAdds.get(i).getName().equals(name))
//                return allRecipesAdds.get(i);
//        }
//
//        return null;
//    }
//
    private void getFromMSP() {
        msp = new MySheredP(context);

        ArrayList<Recipe> AddsRecipes = new ArrayList<>();
        String data = msp.getString(KEY_Account, "NA");
        if (data != "NA") {
            account = new Gson().fromJson(data, Account.class);
            AddsRecipes = account.getRecipesAdds();
            account.setRecipesAdds(AddsRecipes);
        }
        for (int i = 0; i < account.getRecipesAdds().size(); i++) {
            allRecipesAdds.add(account.getRecipesAdds().get(i));
        }
    }


    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }


    public void cleanData() {
        account.clearRecipesAdds();
        allRecipesAdds.clear();
        listGroup.clear();
   //     listItem.clear();
        allAccounts.clear();
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}


