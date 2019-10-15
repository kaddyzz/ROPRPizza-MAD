package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MyProfileActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView emailTextView;
    ImageView profileImageView;
    Button logoutButton;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        setUpProfile();

    }

    //Set up profile from shared pref
    public void setUpProfile() {

        //Initializtions
        nameTextView = findViewById(R.id.textViewName);
        emailTextView = findViewById(R.id.textViewEmail);
        profileImageView = findViewById(R.id.imageViewProfile);
        logoutButton = findViewById(R.id.buttonLogout);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        nameTextView.setText(pref.getString("fullName", ""));
        emailTextView.setText(pref.getString("email", ""));

        // Configure sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);



        Glide.with(MyProfileActivity.this).load(pref.getString("imageURL", "")).into(profileImageView);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut(pref.getInt("loginType", 0));
            }
        });
    }

    public void signOut(int loginType){

        switch(loginType) {
            case 1:
            {


                googleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...

                                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();

                                Toast.makeText(MyProfileActivity.this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MyProfileActivity.this, MainActivity.class));
                            }
                        });


                /*
                mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        Toast.makeText(MyProfileActivity.this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyProfileActivity.this, MainActivity.class));



                        finish();



                    }
                });

*/
            }
                break;
            case 2:
            {

                LoginManager.getInstance().logOut();

                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(MyProfileActivity.this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyProfileActivity.this, MainActivity.class));

            }
                break;
            default:
                break;
        }


    }
}
