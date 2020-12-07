package com.ugur.safemealdeneme.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class CompanyMenuCategoryItemAdapter extends RecyclerView.Adapter<CompanyMenuCategoryItemAdapter.CategoryItemViewHolder> {
    private ArrayList<CompanyMenuCategoryItem> mCategoryItems;

    public static class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menu_category_image_view);
            textView = itemView.findViewById(R.id.menu_category_text);
        }
    }

    public CompanyMenuCategoryItemAdapter(ArrayList<CompanyMenuCategoryItem> categoryItems) {
        mCategoryItems = categoryItems;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_category_item, parent,false);
        CategoryItemViewHolder categoryItemViewHolder = new CategoryItemViewHolder(view);
        return categoryItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        CompanyMenuCategoryItem currentItem = mCategoryItems.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView.setText(currentItem.getCategoryName());
        System.out.println("Current Item Category Name: " + currentItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mCategoryItems.size();
    }

}
