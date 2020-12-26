package com.ugur.safemealdeneme.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Activities.BasketActivity;
import com.ugur.safemealdeneme.Models.CustomerProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class OrderBasketAdapter extends RecyclerView.Adapter<OrderBasketAdapter.OrderBasketViewHolder> {
    private ArrayList<CustomerProductModel> mProductList;

    public static class OrderBasketViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, description, price;
        public Button button;

        public OrderBasketViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.basket_image_view_product);
            title = itemView.findViewById(R.id.basket_text_view_product_title);
            description = itemView.findViewById(R.id.basket_text_view_product_description);
            price = itemView.findViewById(R.id.basket_text_view_product_price);
            button = itemView.findViewById(R.id.basket_button_add_product);
        }
    }

    public OrderBasketAdapter(ArrayList<CustomerProductModel> productList) {
        mProductList = productList;
    }

    @NonNull
    @Override
    public OrderBasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_product,parent,false);
        OrderBasketViewHolder holder = new OrderBasketViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBasketViewHolder holder, final int position) {
        final CustomerProductModel currentItem = mProductList.get(position);
        holder.title.setText(currentItem.getName());
        holder.description.setText(currentItem.getDescription());
        holder.price.setText(currentItem.getPrice() + "$");

        Picasso.with(holder.imageView.getContext())
                .load(currentItem.getImageUri())
                .placeholder(R.drawable.image_selection_border)
                .fit().centerCrop()
                .into(holder.imageView);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductList.remove(position);
                notifyDataSetChanged();
                BasketActivity.setTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}
