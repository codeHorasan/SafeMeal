package com.ugur.safemealdeneme.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ugur.safemealdeneme.Models.DepartmentProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class DepartmentProductAdapter extends RecyclerView.Adapter<DepartmentProductAdapter.DepartmentProductViewHolder> {
    private ArrayList<DepartmentProductModel> mProductList;

    public DepartmentProductAdapter(ArrayList<DepartmentProductModel> productList) {
        mProductList = productList;
    }

    public static class DepartmentProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, description, price;
        public Button button;

        public DepartmentProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_product);
            title = itemView.findViewById(R.id.text_view_product_title);
            description = itemView.findViewById(R.id.text_view_product_description);
            price = itemView.findViewById(R.id.text_view_product_price);
            button = itemView.findViewById(R.id.button_add_product);
        }
    }

    @NonNull
    @Override
    public DepartmentProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_menu_product,parent,false);
        DepartmentProductViewHolder holder = new DepartmentProductViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentProductViewHolder holder, int position) {
        final DepartmentProductModel currentItem = mProductList.get(position);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked!");
            }
        });
        holder.title.setText(currentItem.getName());
        holder.description.setText(currentItem.getDescription());
        holder.price.setText(String.valueOf(currentItem.getPrice()));
        try {
            holder.imageView.setImageURI(currentItem.getImageUri());
        } catch (Exception e) {
            System.out.println("No Image");
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }



}
