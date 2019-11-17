package com.example.roprpizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    int pizzaImage;
    String pizzaName;
    String drinkName = "";
    String allergies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set deafult theme before on create
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        ImageView pizzaImageView = findViewById(R.id.imageViewPizza);
        final Button checkOutButton = findViewById(R.id.buttonConfirm);
        RadioGroup radioGroupDrinks = findViewById(R.id.radioGroupDrinks);
        EditText allergiesEditText = findViewById(R.id.editTextAllergies);

        radioGroupDrinks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radioButtonCoke:
                    {
                        drinkName = "Coke";
                        break;
                    }
                    case R.id.radioButtonPepsi:
                    {
                        drinkName = "Pepsi";
                        break;
                    }
                    case R.id.radioButtonSevenUp:
                    {
                        drinkName = "7 Up";
                        break;
                    }
                }

            }
        });

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkoutButtonAction();
            }
        });


        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            pizzaImage = bundle.getInt("pizzaImage");
            pizzaName = bundle.getString("pizzaName");

            pizzaImageView.setImageResource(pizzaImage);
        }
    }

    //Button methods
    void checkoutButtonAction() {

        //Check for empty fields and then signup

        if (drinkName.equals("")) {
            Toast.makeText(this, "Please select drink.", Toast.LENGTH_SHORT).show();

        } else {

            //Move to next
            Intent moveWithData = new Intent( DetailsActivity.this, SelectAddressActivity.class);
            moveWithData.putExtra("pizzaName", pizzaName);
            moveWithData.putExtra("allergies", allergies);

            startActivity(moveWithData);
        }
    }
}
