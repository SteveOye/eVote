package com.swiftgateicthub.evote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextView mSignUp;
    private EditText mInputEmail;
    private Button mResetPassword;
    ProgressBar pBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mSignUp = findViewById(R.id.link_signup);
        mInputEmail = findViewById(R.id.email_address);
        mResetPassword = findViewById(R.id.reset_password);
        pBar = findViewById(R.id.pBar);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, SignupActivity.class));
            }
        });

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mInputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Enter a registered email", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                pBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //instantiate dialog box
                                    android.app.AlertDialog.Builder resetMail = new android.app.AlertDialog.Builder(ForgotPassword.this);
                                    //Chain together various setter methods to set the dialog characteristics
                                    resetMail.setTitle("Successful");
                                    resetMail.setMessage("Reset Email sent successful!");

                                    resetMail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                    resetMail.show();
                                } else {
                                    if (!task.isSuccessful()) {
                                        android.app.AlertDialog.Builder resetMail = new android.app.AlertDialog.Builder(ForgotPassword.this);
                                        //Chain together various setter methods to set the dialog characteristics
                                        resetMail.setTitle("Failed!");
                                        resetMail.setMessage("Reset password failed! try again.");

                                        resetMail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        resetMail.show();
                                    }
                                    pBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}