package com.example.roprpizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create a view from layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order_history,parent,false);

        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(view);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Bind data from database here

        holder.buttonReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Just a test", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Instanciate all view holder items
        TextView textOrderName;
        TextView textOrderPrice;
        TextView textOrderID;
        TextView textOrderDate;
        Button buttonReorder;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.textOrderName = itemView.findViewById(R.id.textViewPizzaName);
            this.textOrderPrice = itemView.findViewById(R.id.textViewPrice);
            this.textOrderID = itemView.findViewById(R.id.textViewOrderID);
            this.textOrderDate = itemView.findViewById(R.id.textViewDate);
            this.buttonReorder = itemView.findViewById(R.id.buttonReorder);
        }

    }
}
