package com.ugur.safemealdeneme.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Activities.CompanyMenuActivity;
import com.ugur.safemealdeneme.Activities.CompanyMenuProductsActivity;
import com.ugur.safemealdeneme.Models.CompanyMenuCategoryModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class CompanyMenuCategoryItemAdapter extends RecyclerView.Adapter<CompanyMenuCategoryItemAdapter.CategoryItemViewHolder> {
    private ArrayList<CompanyMenuCategoryModel> mCategoryItems;

    public static class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menu_category_image_view);
            textView = itemView.findViewById(R.id.menu_category_text);
            checkBox = itemView.findViewById(R.id.check_box_category);
        }

    }

    public CompanyMenuCategoryItemAdapter(ArrayList<CompanyMenuCategoryModel> categoryItems) {
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
    public void onBindViewHolder(@NonNull final CategoryItemViewHolder holder, final int position) {
        final CompanyMenuCategoryModel currentItem = mCategoryItems.get(position);
        holder.textView.setText(currentItem.getCategoryName());

        Picasso.with(CompanyMenuActivity.context)
                .load(currentItem.getImageUri())
                .placeholder(R.drawable.border_selected)
                .fit().centerCrop()
                .transform(new BlurTransformation(CompanyMenuActivity.context,6,1))
                .into(holder.imageView);

        if (CompanyMenuActivity.ifActionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
            holder.imageView.setAlpha(97);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);
            holder.imageView.setAlpha(1000);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CompanyMenuActivity.ifActionMode) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    if (!holder.checkBox.isChecked()) {
                        holder.checkBox.setChecked(true);
                        CompanyMenuActivity.deletionItems.add(currentItem);
                    } else {
                        holder.checkBox.setChecked(false);
                        CompanyMenuActivity.deletionItems.remove(currentItem);
                    }
                } else {
                    holder.checkBox.setChecked(false);
                    holder.checkBox.setVisibility(View.INVISIBLE);
                    CompanyMenuActivity.deletionItems.clear();
                    Intent intent = new Intent(v.getContext(), CompanyMenuProductsActivity.class);
                    intent.putExtra("uuid", currentItem.getUuid());
                    intent.putExtra("name", currentItem.getCategoryName());
                    v.getContext().startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCategoryItems.size();
    }

}
