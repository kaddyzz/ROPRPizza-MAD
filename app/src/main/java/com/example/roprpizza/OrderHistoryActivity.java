package com.example.roprpizza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class OrderHistoryActivity extends Fragment {


    RecyclerView orderHistoryRecyclerView;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_order_history, container, false);

        orderHistoryAdapter = new OrderHistoryAdapter();

        orderHistoryRecyclerView = view.findViewById(R.id.recyclerViewHistory);

        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);


        return view;
    }
}
