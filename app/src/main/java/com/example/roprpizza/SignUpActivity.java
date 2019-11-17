package com.example.roprpizza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.roprpizza.MainActivity.isValidEmail;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    ImageView imageProfile;

    String imageSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Instansiate values
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);


        Button signUpButton = findViewById(R.id.buttonSignUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAction();
            }
        });

        imageProfile = findViewById(R.id.imageViewProfile);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {

        //Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set title
        builder.setTitle("Notice");

        //Set message
        builder.setMessage("Choose an action");

        //Add the buttons
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openGallery();
            }
        });
        builder.setNegativeButton("Cancel", null);

        //Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {


        if (ContextCompat.checkSelfPermission(SignUpActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SignUpActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        } else {
            // Permission has already been granted

            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, 1);
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:

                    try {
                        //data.getData returns the content URI for the selected Image
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImageInBM = BitmapFactory.decodeStream(imageStream);
                        //image_view.setImageBitmap(selectedImage);
                        imageProfile.setImageBitmap(selectedImageInBM);
                        imageSelected = MyUtilities.encodeTobase64(selectedImageInBM);

                    } catch (FileNotFoundException e) {
                    }

                    break;
            }

    }


    //Button methods
    void signUpAction() {

        //Check for empty fields and then signup

        if (imageSelected.isEmpty()) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();

        }else if (editTextName.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter name.", Toast.LENGTH_SHORT).show();

        } else if (editTextEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();

        } else if (!isValidEmail(editTextEmail.getText().toString())) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();

        } else if (editTextPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();

        } else if (editTextConfirmPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();

        } else {
            //Save details in shared pref
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("fullName", editTextName.getText().toString()); // Storing string
            editor.putString("email", editTextEmail.getText().toString()); // Storing string
            editor.putString("imageURL",imageSelected); // Storing string
            editor.putInt("loginType", 0);

            editor.apply(); // commit changes

            startActivity(new Intent(SignUpActivity.this, NavigationMainActivity.class));
        }
    }

    //Email Regex
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}
