package com.or.mycookingbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.or.mycookingbook.R;
import com.or.mycookingbook.data.Account;

import java.util.ArrayList;

public class Splashscreen extends AppCompatActivity {
   private ProgressBar progress;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private String uuid;
    private ArrayList<Account> allUsers = new ArrayList<>();
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);

        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


        progress = findViewById(R.id.progress);

        readFromDB();


          }


          public void readFromDB(){
              // Read from the database
              myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {

                      // This method is called once with the initial value and again
                      // whenever data at this location is updated.
                      if(dataSnapshot.getValue() != null) {
                          for (DataSnapshot ds : dataSnapshot.getChildren()) {
                              Account tempAccount = ds.getValue(Account.class);
                              if (tempAccount.getUuidAccount().equals(uuid)) {
                                flag = true;
                                  allUsers.add(tempAccount);
                                  openActivityLogedIn();
                              }
                          }


                      }
                      if(!flag)
                      openActivitySignIn();
                  }

                  @Override
                  public void onCancelled(DatabaseError error) {
                      // Failed to read value
                      Log.w("or", "Failed to read value.", error.toException());
                  }
              });


          }

    private void openActivitySignIn() {
        Account t = new Account();
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish();
    }

    private  void openActivityLogedIn(){
        Account t = new Account();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
