package com.ugur.safemealdeneme.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Activities.ChangeProductActivity;
import com.ugur.safemealdeneme.Activities.CompanyMenuProductsActivity;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.DepartmentConstantsClass;
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
        public ImageView settings, deletion;

        public DepartmentProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_product);
            title = itemView.findViewById(R.id.text_view_product_title);
            description = itemView.findViewById(R.id.text_view_product_description);
            price = itemView.findViewById(R.id.text_view_product_price);
            button = itemView.findViewById(R.id.button_add_product);
            settings = itemView.findViewById(R.id.image_product_setting);
            deletion = itemView.findViewById(R.id.image_product_delete);
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
    public void onBindViewHolder(@NonNull DepartmentProductViewHolder holder, final int position) {
        final DepartmentProductModel currentItem = mProductList.get(position);
        System.out.println("Adaptördeyim");
        holder.button.setVisibility(View.GONE);
        holder.title.setText(currentItem.getName());
        holder.description.setText(currentItem.getDescription());
        holder.price.setText(currentItem.getPrice() + "$");

        Picasso.with(CompanyMenuProductsActivity.context)
                .load(currentItem.getImageUri())
                .placeholder(R.drawable.image_selection_border)
                .fit().centerCrop()
                .into(holder.imageView);

        holder.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeIntent = new Intent(v.getContext(), ChangeProductActivity.class);
                changeIntent.putExtra("categoryID",CompanyMenuProductsActivity.categoryID);
                changeIntent.putExtra("productID",currentItem.getProductId());
                changeIntent.putExtra("productName",currentItem.getName());
                changeIntent.putExtra("productDescription",currentItem.getDescription());
                changeIntent.putExtra("productPrice",currentItem.getPrice());
                changeIntent.putExtra("productDate",currentItem.getDateString());
                if (currentItem.getImageUri() != null) {
                    System.out.println("Current ITem Image Urı: " + currentItem.getImageUri());
                    changeIntent.putExtra("productImageURI",currentItem.getImageUri().toString());
                } else {
                    System.out.println("Current Item has no Image Urı");
                }
                v.getContext().startActivity(changeIntent);
            }
        });

        holder.deletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(DepartmentConstantsClass.CURRENT_MENU_UUID).child("Categories")
                        .child(CompanyMenuProductsActivity.categoryID).child("Products").child(currentItem.getProductId()).removeValue();
                mProductList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}
