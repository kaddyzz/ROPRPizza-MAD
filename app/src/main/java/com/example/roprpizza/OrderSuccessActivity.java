package com.example.roprpizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button callRestrauntButton = findViewById(R.id.buttonCall);
        Button goHomeButton = findViewById(R.id.buttonHome);
        TextView paymentMethodTextView = findViewById(R.id.textViewPaymentMethodDescription);

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            paymentMethodTextView.setText(b.getString("paymentMethod"));
        }

        callRestrauntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(OrderSuccessActivity.this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:7783252701")));
                }
                else
                {
                    Toast.makeText(OrderSuccessActivity.this, "Permission not granted.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OrderSuccessActivity.this, NavigationMainActivity.class));
            }
        });
    }


    @Override
    public void onBackPressed() {

        //Do nothing on back press
        Toast.makeText(this, "No further back allowed.", Toast.LENGTH_SHORT).show();
    }
}
