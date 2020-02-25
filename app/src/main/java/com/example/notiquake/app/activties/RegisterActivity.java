package com.example.notiquake.app.activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notiquake.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText password;
    private EditText confirmedPass;
    private TextView alreadyHaveAcc;

    private Button btnSignUp;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userEmail = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_password);
        confirmedPass = findViewById(R.id.ed_confirm_password);
        alreadyHaveAcc = findViewById(R.id.tvAlreadyHaveAcc);

        btnSignUp = findViewById(R.id.btnRegSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = userEmail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String secondPass = confirmedPass.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Required Filed");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(secondPass)){
                    confirmedPass.setError("Required Field");
                    return;
                }

                if((pass.trim().equals(secondPass.trim()))){
                    progressDialog.setMessage("Signing Up");
                    progressDialog.show();


                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                            Toast.makeText(getApplicationContext(),R.string.successfulAlert,Toast.LENGTH_LONG).show();
                                            userEmail.setText("");
                                            password.setText("");
                                            confirmedPass.setText("");
                                        }else {
                                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {
                                progressDialog.dismiss();
                                Snackbar.make(v,task.getException().getMessage(),Snackbar.LENGTH_LONG)
                                        .setAction(R.string.retryTxt, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                password.setText(R.string.emptyEditTex);
                                            }
                                        }).setActionTextColor(Color.RED)
                                        .show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(),R.string.passwordError,Toast.LENGTH_LONG).show();
                    confirmedPass.setText(R.string.emptyEditTex);
                }

            }
        });

        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });



    }
}
