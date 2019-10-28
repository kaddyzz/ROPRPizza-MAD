package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {


    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    ArrayList<PizzaTypesModelClass> pizzaData;

    public static final String[] pizzaName= {"Pepperoni","Sausage", "Cheese Pizza","Hawaiian","Chicken Bacon","Garden Fresh","Six Cheese","Spinach Alferdo"};
    public static final int[] pizzaImages = {R.drawable.p11,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        pizzaData = new ArrayList<>();

        for(int i=0;i<pizzaName.length;i++)
        {
            PizzaTypesModelClass pizzaDataObj = new PizzaTypesModelClass(pizzaName[i],pizzaImages[i]);

            pizzaData.add(pizzaDataObj);
        }

        homeAdapter = new HomeAdapter(pizzaData);
        recyclerView = view.findViewById(R.id.recyclerViewPizzas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeAdapter);


        return view;
    }


}
