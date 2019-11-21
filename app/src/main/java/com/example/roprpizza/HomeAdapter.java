package com.example.roprpizza;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(@NonNull final HomeAdapter.ViewHolder holder, final int position) {

        final PizzaTypesModelClass pizzaData1 = pizzaList.get(positionIndex);
        final PizzaTypesModelClass pizzaData2 = pizzaList.get(positionIndex + 1);


        //Set pizza name  and image on left
        holder.textPizza1.setText(pizzaData1.getPizzaName());
        Glide.with(context).load(pizzaData1.getImageOfPizza()).into(holder.imagePizza1);

        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Move to next
                Intent moveWithData = new Intent( context, DetailsActivity.class);
                moveWithData.putExtra("pizzaImage", pizzaData1.getImageOfPizza());
                moveWithData.putExtra("pizzaName", pizzaData1.getPizzaName());

                context.startActivity(moveWithData);
            }
        });

        //Set pizza name and image or right
        holder.textPizza2.setText(pizzaData2.getPizzaName());
        Glide.with(context).load(pizzaData2.getImageOfPizza()).into(holder.imagePizza2);

        holder.cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Move to next
                Intent moveWithData = new Intent( context, DetailsActivity.class);
                moveWithData.putExtra("pizzaImage", pizzaData2.getImageOfPizza());
                moveWithData.putExtra("pizzaName", pizzaData2.getPizzaName());

                context.startActivity(moveWithData);
            }
        });

        positionIndex += 2;
    }


    @Override
    public int getItemCount() {
        return pizzaList.size()/2;
    }
}
