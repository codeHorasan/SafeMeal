package com.ugur.safemealdeneme.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Models.CustomerMenuCategoryModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class CustomerMenuCategoryItemAdapter extends RecyclerView.Adapter<CustomerMenuCategoryItemAdapter.CustomerMenuCategoryViewHolder> {
    private ArrayList<CustomerMenuCategoryModel> mCategoryItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CustomerMenuCategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CustomerMenuCategoryViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.customer_category_image_view);
            textView = itemView.findViewById(R.id.customer_category_text_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            try {
                                listener.onItemClick(position);
                            } catch (Exception e) {
                                System.out.println("HATA: " + e.getCause());
                            }
                        }
                    }
                }
            });
        }
    }

    public CustomerMenuCategoryItemAdapter(ArrayList<CustomerMenuCategoryModel> categoryItems) {
        mCategoryItems = categoryItems;
    }

    @NonNull
    @Override
    public CustomerMenuCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_category_item, parent,false);
        CustomerMenuCategoryItemAdapter.CustomerMenuCategoryViewHolder viewHolder = new CustomerMenuCategoryItemAdapter.CustomerMenuCategoryViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerMenuCategoryViewHolder holder, int position) {
        final CustomerMenuCategoryModel currentItem = mCategoryItems.get(position);
        holder.textView.setText(currentItem.getCategoryName());

        Picasso.with(holder.imageView.getContext())
                .load(currentItem.getImageUri())
                .placeholder(R.drawable.border_selected)
                .fit().centerCrop()
                .transform(new BlurTransformation(holder.imageView.getContext(),6,1))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mCategoryItems.size();
    }
}
