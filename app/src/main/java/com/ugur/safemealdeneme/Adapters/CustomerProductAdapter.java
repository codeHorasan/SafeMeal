package com.ugur.safemealdeneme.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Customer;
import com.ugur.safemealdeneme.Models.CustomerProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class CustomerProductAdapter extends RecyclerView.Adapter<CustomerProductAdapter.CustomerProductViewHolder> implements Filterable {
    private ArrayList<CustomerProductModel> mProductList;
    private ArrayList<CustomerProductModel> mProductListFull;

    public static class CustomerProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, description, price;
        public Button button;

        public CustomerProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.customer_image_view_product);
            title = itemView.findViewById(R.id.customer_text_view_product_title);
            description = itemView.findViewById(R.id.customer_text_view_product_description);
            price = itemView.findViewById(R.id.customer_text_view_product_price);
            button = itemView.findViewById(R.id.customer_button_add_product);
        }
    }

    public CustomerProductAdapter(ArrayList<CustomerProductModel> productList) {
        mProductList = productList;
        mProductListFull = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public CustomerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_menu_product,parent,false);
        CustomerProductViewHolder holder = new CustomerProductViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerProductViewHolder holder, int position) {
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
                Customer customer = Customer.getInstance();
                customer.getProductModelList().add(currentItem);
                StyleableToast.makeText(v.getContext(),"Product Added Successfully!",R.style.SuccessToast).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    @Override
    public Filter getFilter() {
        return customerFilter;
    }

    private Filter customerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CustomerProductModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mProductListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CustomerProductModel model : mProductListFull) {
                    if (model.getName().toLowerCase().contains(filterPattern.toLowerCase()) || model.getDescription().toLowerCase().contains(filterPattern.toLowerCase())) {
                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mProductList.clear();
            mProductList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
