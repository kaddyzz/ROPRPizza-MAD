package com.example.roprpizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    ArrayList<PizzaTypesModelClass> pizzaData;

    public static final String[] pizzaName= {"Breaking Bad","Rick and Morty", "FRIENDS","Sherlock","Stranger Things"};
    public static final int[] pizzaImages = {R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pizzaData = new ArrayList<>();

        for(int i=0;i<pizzaName.length;i++)
        {
            PizzaTypesModelClass pizzaDataObj = new PizzaTypesModelClass();

            pizzaDataObj.setPizzaName(pizzaName[i]);
            pizzaDataObj.setImageOfPizza(pizzaImages[i]);

            pizzaData.add(pizzaDataObj);
        }

        homeAdapter = new HomeAdapter(pizzaData);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewPizzas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeAdapter);
    }
}
