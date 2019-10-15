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


    private List<PizzaTypesModelClass> pizzaList;
    private Context context;

    private int positionIndex = 0;


    public HomeAdapter(List<PizzaTypesModelClass> pizzaList)
    {
        this.pizzaList = pizzaList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imagePizza1;
        TextView textPizza1;
        CardView cardView1;

        ImageView imagePizza2;
        TextView textPizza2;
        CardView cardView2;


        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imagePizza1 = itemView.findViewById(R.id.imagePizza1);
            this.textPizza1 = itemView.findViewById(R.id.namePizza1);
            this.cardView1 = itemView.findViewById(R.id.cardViewItem1);

            this.imagePizza2 = itemView.findViewById(R.id.imagePizza2);
            this.textPizza2 = itemView.findViewById(R.id.namePizza2);
            this.cardView2 = itemView.findViewById(R.id.cardViewItem2);
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

        PizzaTypesModelClass pizzaData1 = pizzaList.get(positionIndex);
        PizzaTypesModelClass pizzaData2 = pizzaList.get(positionIndex + 1);


        holder.textPizza1.setText(pizzaData1.getPizzaName());
        holder.imagePizza1.setImageResource(pizzaData1.getImageOfPizza());
        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"The position is:" + position,Toast.LENGTH_SHORT).show();
            }
        });

        holder.textPizza2.setText(pizzaData2.getPizzaName());
        holder.imagePizza2.setImageResource(pizzaData2.getImageOfPizza());
        holder.cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"The position is:" + position+1,Toast.LENGTH_SHORT).show();
            }
        });

        positionIndex += 2;
    }

    @Override
    public int getItemCount() {
        return pizzaList.size()/2;
    }
}
