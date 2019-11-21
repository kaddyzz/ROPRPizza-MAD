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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {


    RecyclerView recyclerView;
    HomeAdapter homeAdapter;

    private FirebaseFirestore db;
    //Progress HUD
    KProgressHUD kProgressHUD;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home, container, false);

        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Create HUD
        kProgressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting Pizza")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        //Create modal class object
        final List<PizzaTypesModelClass> pizzaList = new ArrayList<>();

        //Start HUD
        kProgressHUD.show();

        //Get pizza from firebase
        db.collection("pizzaHome")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //Dismiss HUD
                        kProgressHUD.dismiss();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FIREBASE", document.getId() + " => " + document.get("pizzaName"));

                                //Add each pizza to list
                                PizzaTypesModelClass pizzaDataObj = new PizzaTypesModelClass(document.get("pizzaName").toString(),document.get("pizzaImage").toString());

                                pizzaList.add(pizzaDataObj);
                            }

                            homeAdapter = new HomeAdapter(pizzaList);
                            recyclerView = view.findViewById(R.id.recyclerViewPizzas);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(homeAdapter);

                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return view;
    }


}
