package com.example.roprpizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    List<PizzaTypesModelClass> pizzaList;
    Context context;

    public HomeAdapter(List<PizzaTypesModelClass> pizzaList)
    {
        this.pizzaList = pizzaList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imagePizza;
        TextView textPizza;
        CardView cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imagePizza = (ImageView)itemView.findViewById(R.id.imagePizza);
            this.textPizza = (TextView)itemView.findViewById(R.id.namePizza);
            this.cardView = (CardView)itemView.findViewById(R.id.cardViewItem);
        }

    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);

        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(view);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, final int position) {

        PizzaTypesModelClass pizzaData = pizzaList.get(position);

        holder.textPizza.setText(pizzaData.getPizzaName());
        holder.imagePizza.setImageResource(pizzaData.getImageOfPizza());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"The position is:" + position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }
}
