package com.swiftgateicthub.evote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
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
                    Snackbar.make(v, "Enter your first name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    Snackbar.make(v, "Enter your last name", Snackbar.LENGTH_LONG)
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
                        sendVerificationEmail();
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

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            AlertDialog.Builder verifyAcct = new AlertDialog.Builder(SignupActivity.this);
                            //Chain together various setter methods to set the dialog characteristics
                            verifyAcct.setTitle("Account Verification");
                            verifyAcct.setMessage(getString(R.string.mailSent));

                            verifyAcct.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            verifyAcct.show();
                            // after email is sent just logout the user and finish this activity
//                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });
    }
}
