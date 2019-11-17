package com.example.roprpizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Google sign in
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    //Facebook sign in
    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    Button loginButton;
    Button signUpButton;
    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initializations
        callGoogleSignIn();
        callFacebookSignIn();

        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAction();
            }
        });


        passwordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */


                    loginAction();
                    handled = true;
                }
                return handled;
            }
        });

    }



    @Override
    public void onBackPressed() {

        //Do nothing on back press
        Toast.makeText(this, "No further back allowed.", Toast.LENGTH_SHORT).show();
    }

    //Button methods
    void loginAction(){

        //Check for empty fields and then login

        if (emailField.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(emailField.getText().toString()))
        {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if(passwordField.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Save details in shared pref
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("fullName","Test User"); // Storing string
            editor.putString("email",emailField.getText().toString() ); // Storing string
            editor.putString("imageURL",""); // Storing string
            editor.putInt("loginType",0);

            editor.apply(); // commit changes

            startActivity(new Intent(MainActivity.this, NavigationMainActivity.class));
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    void signUpAction(){

        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

    //Google Methods
    void callGoogleSignIn()
    {
        //Client ID
        //889523807575-b15h91h19q627ee1lk7j9q1chn0qhks1.apps.googleusercontent.com
        //Client Secret
        //2QHtx9SGeWgclk59-fS1m8FU

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);



        // Configure sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //startActivity(new Intent(this, MyProfileActivity.class));

            try {
                String fullName = account.getDisplayName();
                String email = account.getEmail();
                Uri imageURL = account.getPhotoUrl();

                //Save details in shared pref
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("fullName", fullName); // Storing string
                editor.putString("email", email); // Storing string
                editor.putString("imageURL", imageURL.toString()); // Storing string
                editor.putInt("loginType",1);

                editor.apply(); // commit changes

                startActivity(new Intent(this, NavigationMainActivity.class));
            }
            catch (Exception ex)
            {
                Toast.makeText(this, "Error occured!!", Toast.LENGTH_SHORT).show();
            }


        } catch (ApiException e) {
            Log.w("GoogleSignInError", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {

        //Check for existing account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
        {
          //  startActivity(new Intent(MainActivity.this, NavigationMainActivity.class));
        }

        super.onStart();
    }

    //Facebook methods
    void callFacebookSignIn()
    {
        fbLoginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

       // loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        fbLoginButton.setPermissions(Arrays.asList("email","public_profile"));

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null)
            {
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    //Crete get data
                    String firstName = object.getString("first_name");
                    String lastName = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    String imageURL = "https://graph.facebook.com/" + id + "/picture?type=normal";


                    //Save details in shared pref
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("fullName", firstName+lastName); // Storing string
                    editor.putString("email", email); // Storing string
                    editor.putString("imageURL", imageURL); // Storing string
                    editor.putInt("loginType",2);

                    editor.apply(); // commit changes

                    startActivity(new Intent(MainActivity.this, NavigationMainActivity.class));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();

        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();


    }





}
