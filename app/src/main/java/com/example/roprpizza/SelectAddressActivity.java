package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectAddressActivity extends AppCompatActivity {

    Geocoder geocoder;
    List<Address> addresses;
    String pizzaName;
    String pizzaAllergies;

    private FusedLocationProviderClient fusedLocationClient;

    Button fetchAddressButton;
    Button paymentButton;
    EditText aptNumberEditText;
    EditText cityEditText;
    EditText postalEditText;
    EditText provinceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        //Instanciate
        fetchAddressButton = findViewById(R.id.buttonUpdateLocation);
        paymentButton = findViewById(R.id.buttonCheckout);
        aptNumberEditText = findViewById(R.id.editTextHouseNumber);
        cityEditText = findViewById(R.id.editTextCity);
        postalEditText = findViewById(R.id.editTextPostal);
        provinceEditText = findViewById(R.id.editTextProvince);


        //Location client, get last location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //Fetch address button
        fetchAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle permissions
                askForPermission();
            }
        });

        //Check address and move to payment
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call method to check empty values
                paymentAction();

            }
        });

        //Get last data
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            pizzaName = bundle.getString("pizzaName");
            pizzaAllergies = bundle.getString("allergies");

        }
    }


    private void askForPermission() {

        //If not ask for permission

        if (ContextCompat.checkSelfPermission(SelectAddressActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SelectAddressActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

        } else {
            // Permission has already been granted
            fetchLocation();
        }

    }

    private  void fetchLocation()
    {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Double latittude = location.getLatitude();
                            Double longitude = location.getLongitude();

                            //Set geocoder to user location
                            geocoder = new Geocoder(SelectAddressActivity.this, Locale.getDefault());

                            try {
                                //Fetch address in address list
                                addresses = geocoder.getFromLocation(latittude, longitude, 1);

                                String address = addresses.get(0).getAddressLine(0);
                                String area = addresses.get(0).getLocality();
                                String province = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postal = addresses.get(0).getPostalCode();
                                String subAptNumber = addresses.get(0).getSubThoroughfare();
                                String aptNumber = addresses.get(0).getThoroughfare();


                                aptNumberEditText.setText(subAptNumber + " " + aptNumber);
                                cityEditText.setText(area);
                                postalEditText.setText(postal);
                                provinceEditText.setText(province);

                            } catch (IOException e) {
                                Log.e("ROPR Pizza : Location", "IOException : " + e );
                                Toast.makeText(SelectAddressActivity.this, "Unable to get the location!", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else {
                            Toast.makeText(SelectAddressActivity.this, "Unable to get the location!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectAddressActivity.this, "Unable to get the location!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //Button methods
    void paymentAction() {

        //Check for empty fields and then to payment activity

        if (aptNumberEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter apartment or building number.", Toast.LENGTH_SHORT).show();

        } else if (cityEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter city.", Toast.LENGTH_SHORT).show();

        } else if (postalEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter postal address.", Toast.LENGTH_SHORT).show();

        } else if (provinceEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter province.", Toast.LENGTH_SHORT).show();

        } else {
            //Move to next
            Intent moveWithData = new Intent( SelectAddressActivity.this, PaymentActivity.class);
            moveWithData.putExtra("pizzaName", pizzaName);
            moveWithData.putExtra("allergies", pizzaAllergies);
            moveWithData.putExtra("address", aptNumberEditText.getText().toString() + " " +cityEditText.getText().toString() + " " + postalEditText.getText().toString());


            startActivity(moveWithData);
        }
    }



}
