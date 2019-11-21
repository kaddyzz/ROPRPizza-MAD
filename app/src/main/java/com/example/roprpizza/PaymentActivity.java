package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PaymentActivity extends AppCompatActivity {

    PayPalConfiguration config;
    String pizzaName;
    String pizzaAllergies;
    String address;

    private FirebaseFirestore db;
    KProgressHUD kProgressHUD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = FirebaseFirestore.getInstance();

        config = new PayPalConfiguration()

                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId("AQHxDheK516SuS-E--l-aN_3hEQfSb88uAEnmUU0hDwlWSZFvRFe1p72y6pB2OwT1XTv4he7byLJ5QEF");

        Button paypalPaymentButton = findViewById(R.id.buttonPaymentPaypal);
        Button codPaymentButton = findViewById(R.id.buttonPaymentCOD);


        paypalPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuyPressed(v);
            }
        });

        codPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move to next
                orderPlaced("Cash On Delivery");
            }
        });

        //Get last data
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            pizzaName = bundle.getString("pizzaName");
            pizzaAllergies = bundle.getString("allergies");
            address = bundle.getString("address");
        }

        kProgressHUD = KProgressHUD.create(PaymentActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Placing your order")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }



    public void onBuyPressed(View pressed) {

        PayPalPayment payment = new PayPalPayment(new BigDecimal("14.00"), "CAD", pizzaName,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    //Add into firebase
                    orderPlaced("Paypal");

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    public void orderPlaced(final String paymentMethod)
    {

        //Get current date for order date
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String currentDate = df.format(Calendar.getInstance().getTime());

        //Get id
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        kProgressHUD.show();

        //Map all the values
        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("userID",pref.getString("userID", ""));
        orderInfo.put("userPrice","$14");
        orderInfo.put("userPizzaName",pizzaName);
        orderInfo.put("userDate",currentDate);
        orderInfo.put("userAllergies", pizzaAllergies);
        orderInfo.put("userAddress", address);
        orderInfo.put("paymentMethod",paymentMethod);


        // Add a new document for new order
        db.collection("usersOrders")
                .add(orderInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        //Dismiss HUD
                        kProgressHUD.dismiss();

                        Intent moveWithData = new Intent( PaymentActivity.this, OrderSuccessActivity.class);
                        moveWithData.putExtra("paymentMethod", paymentMethod);

                        startActivity(moveWithData);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase: ", "Error adding user", e);
                    }
                });
    }
}


