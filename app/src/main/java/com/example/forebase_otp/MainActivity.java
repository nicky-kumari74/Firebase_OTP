package com.example.forebase_otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
Button send;
EditText number;
private FirebaseAuth mauth;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseApp.initializeApp(this);
        number=findViewById(R.id.number);
        send=findViewById(R.id.send);
        mauth=FirebaseAuth.getInstance();
        ProgressBar progressBar=findViewById(R.id.progress_bar);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=number.getText().toString();
                if(s.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                }
                else if(!s.matches("[6-9][0-9]{9}")){
                    number.requestFocus();
                    number.setError("Enter Valid Number");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    send.setVisibility(View.INVISIBLE);
                           mcallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String backend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(MainActivity.this, otppage.class);
                                    i.putExtra("number",s);
                                    i.putExtra("backend",backend);
                                    startActivity(i);
                                    number.setText("");

                                }
                            };
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mauth)
                                    .setPhoneNumber("+91"+number.getText().toString().trim())
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(MainActivity.this)
                                    .setCallbacks(mcallback)
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });
    }
}