package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrderHistoryActivity extends Fragment {


    RecyclerView orderHistoryRecyclerView;
    OrderHistoryAdapter orderHistoryAdapter;

    private FirebaseFirestore db;
    //Progress HUD
    KProgressHUD kProgressHUD;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_order_history, container, false);

        //Create modal class object
        final List<OrderHistoryModal> orderList = new ArrayList<>();

        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Create HUD
        kProgressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Getting order history")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        kProgressHUD.show();

        //Get id
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        final TextView titleTextView = view.findViewById(R.id.textViewOrderHistoryTitle);

        //Get pizza from firebase
        db.collection("usersOrders")
                .whereEqualTo("userID",pref.getString("userID", ""))
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
                                OrderHistoryModal orderHistoryObj = new OrderHistoryModal(document.get("userPizzaName").toString(),document.get("userPrice").toString(),"Order Placed",document.get("userDate").toString(),document.get("userAllergies").toString(),document.getId().toString());

                                orderList.add(orderHistoryObj);
                            }


                            if (orderList.isEmpty())
                            {
                                titleTextView.setText("You haven't placed any orders yet!");
                            }
                            else
                            {
                                orderHistoryAdapter = new OrderHistoryAdapter(orderList);

                                orderHistoryRecyclerView = view.findViewById(R.id.recyclerViewHistory);

                                orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

                                orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });





        return view;
    }
}
