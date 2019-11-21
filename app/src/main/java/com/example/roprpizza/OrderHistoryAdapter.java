package com.example.roprpizza;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<OrderHistoryModal> orderList;


    public OrderHistoryAdapter(List<OrderHistoryModal> orderList)
    {
        this.orderList = orderList;
    }

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

        final OrderHistoryModal orderData = orderList.get(position);

        holder.buttonReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move to next
                Intent moveWithData = new Intent( context, SelectAddressActivity.class);
                moveWithData.putExtra("pizzaName", orderData.getOrderName());
                moveWithData.putExtra("allergies", orderData.getOrderAllergies());

                context.startActivity(moveWithData);
            }
        });

        holder.textOrderName.setText(orderData.getOrderName());
        holder.textOrderPrice.setText(orderData.getOrderPrice());
        holder.textOrderDate.setText(orderData.getOrderDate());
        holder.textOrderID.setText("#" + orderData.getOrderID().substring(0,6));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
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
