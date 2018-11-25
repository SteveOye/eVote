package com.swiftgateicthub.evote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    public static final String LOG_TAG = SignupActivity.class.getName();
    private TextView inputFstName, inputLstName, inputEmail, inputPassword;
    private ProgressBar progressBar;
    Button sign_up;

    //Firebase
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sign_up = findViewById(R.id.sign_up);
        inputFstName = findViewById(R.id.firstName);
        inputLstName = findViewById(R.id.lastName);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        //Get Firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = inputFstName.getText().toString().trim();
                String lastName = inputLstName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    Snackbar.make(v, "Enter a Name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    Snackbar.make(v, "Enter a Name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Enter Email address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Enter Password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                if (password.length() < 6) {
                    Snackbar.make(v, "Password is too short, enter a minimum of 6 characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
