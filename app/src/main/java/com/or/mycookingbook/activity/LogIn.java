package com.or.mycookingbook.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.or.mycookingbook.R;
import com.or.mycookingbook.data.Account;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class LogIn extends AppCompatActivity {
    private ArrayList<Account> allUsers = new ArrayList<>();

    private Button login_send_code, log_in_BTN_send;
    private View view;
    private EditText login_TXT_phone_number,login_TXT_password;
    private String codeSent, uuid;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Account newAccount;
    FirebaseUser user;
    private Boolean flagSendToPhone= false;

    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_log_in);

          findViews(view);

//////////////////////////
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted();
            }


            uuid = android.provider.Settings.Secure.getString(
                    this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

      //       Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

             user =FirebaseAuth.getInstance().getCurrentUser();
        //    user.delete();



          setImages();
            login_TXT_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setImages();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            login_TXT_phone_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setImages();

                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        login_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if(checkPhoneNumber(login_TXT_phone_number.getText().toString()))
                sendVerificationCode();
                else
                    login_TXT_phone_number.setError("מספר טלפון לא חוקי");
            }
        });

        log_in_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               view.startAnimation(buttonClick);
               String s = log_in_BTN_send.getText().toString();

               if (login_TXT_password.getText().toString().length() >0)
                   if((login_TXT_phone_number.getText().toString().length()>0)&&flagSendToPhone)
                        verifySignInCode();
                   else
                       login_TXT_phone_number.setError("מספר טלפון לא חוקי");
               else
                   login_TXT_password.setError("קוד לא חוקי");
            }
        });
        }

        private void findViews (View view){
            login_send_code = findViewById(R.id.login_send_code);
            log_in_BTN_send = findViewById(R.id.log_in_BTN_send);
            login_TXT_phone_number = findViewById(R.id.login_TXT_phone_number);
            login_TXT_password = findViewById(R.id.login_TXT_password);

        }

        public boolean checkPhoneNumber (String phone){
            if (phone.length() != 10)
                return false;
            for (int i = 0; i < phone.length(); i++) {
                if (phone.charAt(i) < 0 && phone.charAt(i) > 10) {
                    return false;
                }
            }
            if (phone.charAt(0) != '0')
                return false;
            return true;
        }

        public void setImages(){
            if(checkPhoneNumber(login_TXT_phone_number.getText().toString()))
                login_TXT_phone_number.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.ic_action_ok, 0,R.drawable.ic_action_phone,0);

            else
                login_TXT_phone_number.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.ic_action_no, 0,R.drawable.ic_action_phone,0);


            if((login_TXT_password.getText().toString()).length() == 6)
                login_TXT_password.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.ic_action_ok, 0,R.drawable.ic_action_code,0);

            else
                login_TXT_password.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.ic_action_no, 0,R.drawable.ic_action_code,0);


        }
/////////////////////////////////////////////////////////////

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }



    private String buildPhoneNumber() {
        String phone = login_TXT_phone_number.getText().toString();
        String newPhone = "+972";
        if(phone.charAt(0) != '0')
            newPhone += phone.charAt(0);

        for (int i =1; i<phone.length(); i++)
        {
            newPhone += phone.charAt(i);
        }
        return newPhone;
    }


    private void sendVerificationCode() {
        String phone= buildPhoneNumber();
            flagSendToPhone = true;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    private void verifySignInCode() {
        String code =login_TXT_password.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //here you can open new activity
                            newAccount = new Account(buildPhoneNumber(),uuid);
//                            allAccounts.new_add_one_ingr(newAccount);
                            myRef.child("Users").child(newAccount.getUserPhoneNumber()).setValue(newAccount);
                            openActivityLogedIn();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                login_send_code.setError("קוד לא חוקי");
                            }
                        }
                    }
                });
    }


    private  void openActivityLogedIn(){
        Account t = new Account();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}


