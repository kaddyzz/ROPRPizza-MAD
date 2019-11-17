package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NavigationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    GoogleSignInClient googleSignInClient;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);

        //Tell app to remove defaul action bar and use tool bar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get header view to load data
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.imageProfile);
        TextView profileName = headerView.findViewById(R.id.nameProfile);
        TextView profileEmail = headerView.findViewById(R.id.emailProfile);

        //Call shared  pref to get data profile
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        profileName.setText(pref.getString("fullName", ""));
        profileEmail.setText(pref.getString("email", ""));

        if (pref.getInt("loginType", 0) == 0)
        {
            profileImage.setImageBitmap(MyUtilities.decodeBase64(pref.getString("imageURL", "")));
        }
        else
        {
            Glide.with(this).load(pref.getString("imageURL", "")).into(profileImage);
        }



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Configure sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileActivity()).commit();
                break;

            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHistoryActivity()).commit();
                break;

            case R.id.nav_share:
                //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing application to order pizza! - ROPR Pizza"); // Simple text and URL to share
                sendIntent.setType("text/plain");
                this.startActivity(sendIntent);
                break;

            case R.id.nav_logout:
                signOut(pref.getInt("loginType", 0));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



    //Handle logout
    public void signOut(int loginType) {

        switch (loginType) {
            case 0: {
                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
            break;
            case 1: {
                googleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...

                                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();

                                Toast.makeText(NavigationMainActivity.this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NavigationMainActivity.this, MainActivity.class));
                            }
                        });

            }
            break;
            case 2: {

                LoginManager.getInstance().logOut();

                final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(this, "Successfull signed out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));

            }
            break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
    }
