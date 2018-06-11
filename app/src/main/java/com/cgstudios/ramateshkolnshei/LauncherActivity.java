package com.cgstudios.ramateshkolnshei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class LauncherActivity extends AppCompatActivity {

    public static final String TAG = "LauncherActivity";
    private static final String LOGIN_KEY = "LOGIN_KEY";
    public static final int RC_SIGN_IN = 1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference entryReference = database.getReference().child("emails");
    private SharedPreferences sp;

    private ProgressBar progressBarLogin;
    private TextView emailValidationTextView;
    private TextView emailNotFoundTextView;
    private TextView contactTextView;
    private Button loginButton;

    private boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        verified = false;
        progressBarLogin = findViewById(R.id.progressBar_login);
        emailValidationTextView = findViewById(R.id.email_validation_text);
        emailNotFoundTextView = findViewById(R.id.email_not_found_text);
        loginButton = findViewById(R.id.login_button);
        contactTextView = findViewById(R.id.contact_info_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailNotFoundTextView.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                contactTextView.setVisibility(View.GONE);
                signIn();
            }
        });


        // Check if already logged in
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean(LOGIN_KEY, false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            signIn();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                progressBarLogin.setVisibility(View.VISIBLE);
                emailValidationTextView.setVisibility(View.VISIBLE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                Log.v(TAG, email);
                verifyEmail(email);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void verifyEmail(final String email) {
        // Load emails from the spreadsheet and check for match
        entryReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Account account = dataSnapshot.getValue(Account.class);
                if (account.getEmail().compareToIgnoreCase(email) == 0) {
                    Log.v(TAG, "Found a match");
                    verified = true;
                    // Save 'logged in' status
                    sp = getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean(LOGIN_KEY, true).apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // After all data has been loaded and no match was found, bring up 'error' screen
        entryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!verified) {
                    progressBarLogin.setVisibility(View.GONE);
                    emailValidationTextView.setVisibility(View.GONE);

                    emailNotFoundTextView.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    contactTextView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }
}
