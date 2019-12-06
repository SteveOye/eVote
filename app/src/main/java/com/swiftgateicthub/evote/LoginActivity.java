package com.swiftgateicthub.evote;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public static final java.lang.String LOG_TAG = LoginActivity.class.getName();

    private Button sign_in_bt;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView mSignUp, mForgotPassword;
    private ProgressBar progressBar;

    //firebase
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init views
        sign_in_bt = findViewById(R.id.sign_in);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        mSignUp = findViewById(R.id.link_signup);
        mForgotPassword = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progressBar);

        //Get Firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        sign_in_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

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

                // there was an error
                if (password.length() < 6) {
                    Snackbar.make(v, (getString(R.string.minimum_password)), Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //authenticate user
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    //instantiate dialog box
                                    AlertDialog.Builder signInError = new AlertDialog.Builder(LoginActivity.this);
                                    //Chain together various setter methods to set the dialog characteristics
                                    signInError.setTitle("Sign in failed");
                                    signInError.setMessage(getString(R.string.auth_failed));

                                    signInError.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    signInError.show();
                                } else {
                                    checkIfEmailVerified();
                                }
                            }
                        });
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            //instantiate dialog box
            AlertDialog.Builder verifyAcct = new AlertDialog.Builder(LoginActivity.this);
            //Chain together various setter methods to set the dialog characteristics
            verifyAcct.setTitle("Account Verification");
            verifyAcct.setMessage(getString(R.string.notVerify));

            verifyAcct.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            verifyAcct.show();

            FirebaseAuth.getInstance().signOut();
            //restart this activity
        }
    }
}

