package com.example.notiquake.app.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notiquake.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PrefsFile";


    private EditText userEmail;
    private EditText userPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private String password;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userEmail = findViewById(R.id.ed_username);
        userPassword = findViewById(R.id.ed_password);
        TextView resetPasswordTv = findViewById(R.id.tvResetPassword);
        btnSignIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnRegister);


        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    userPassword.setError("Required Field");
                    return;
                }

                progressDialog.setMessage("Signing In");
                progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(),ListEarthquakesActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_LONG).show();
                                    userEmail.setText("");
                                    userPassword.setText("");
                                }
                            } else {

                                progressDialog.dismiss();
                                Snackbar.make(v,R.string.failedLoginTxt,Snackbar.LENGTH_LONG).setAction(R.string.retryTxt, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userEmail.setText(R.string.emptyEditTex);
                                        userPassword.setText(R.string.emptyEditTex);
                                    }
                                }).setActionTextColor(Color.RED).show();

                            }
                        }
                    });
                }
        });

        resetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPassActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);

            }
        });
    }

}
