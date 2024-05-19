package com.example.forebase_otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class otppage extends AppCompatActivity {
TextView show,timer,resend,text;
EditText otp1,otp2,otp3,otp4,otp5,otp6;
String getotpback,num;
Button verify;
private FirebaseAuth mauth;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        mauth=FirebaseAuth.getInstance();
        show=findViewById(R.id.show);
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp5=findViewById(R.id.otp5);
        otp6=findViewById(R.id.otp6);
        verify=findViewById(R.id.verify);
        timer=findViewById(R.id.timer);
        resend=findViewById(R.id.resend);
        text=findViewById(R.id.texts);
        ProgressBar progressBar=findViewById(R.id.progress_bar2);
        num=getIntent().getStringExtra("number");
        show.setText(String.format("+91-%s",num));
        getotpback=getIntent().getStringExtra("backend");
        settimer();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otp1.getText().toString().isEmpty() && !otp2.getText().toString().isEmpty() && !otp3.getText().toString().isEmpty() && !otp4.getText().toString().isEmpty() && !otp5.getText().toString().isEmpty() && !otp6.getText().toString().isEmpty()){
                    String entercodeotp=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                    if(getotpback!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        verify.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(getotpback,entercodeotp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        verify.setVisibility(View.VISIBLE);
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(otppage.this,verified.class));
                                        }
                                        else{
                                            Toast.makeText(otppage.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(otppage.this, "check Internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        numberotpmove();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                verify.setVisibility(View.INVISIBLE);
                resend.setVisibility(View.GONE);
                mcallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.INVISIBLE);
                        verify.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(otppage.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        verify.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        timer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String backend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        getotpback=backend;
                        progressBar.setVisibility(View.INVISIBLE);
                        verify.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        timer.setVisibility(View.VISIBLE);
                        settimer();
                    }
                };
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mauth)
                                .setPhoneNumber("+91"+num)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(otppage.this)
                                .setCallbacks(mcallback)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });
    }

    private void settimer() {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int t=(int)(millisUntilFinished/1000);
                if(t<10){
                    timer.setText("0:0"+String.valueOf(t));
                }
                else
                timer.setText("0:"+String.valueOf(t));
            }

            @Override
            public void onFinish() {
                timer.setVisibility(View.GONE);
                text.setText("Resend OTP? click on ");
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void numberotpmove() {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}