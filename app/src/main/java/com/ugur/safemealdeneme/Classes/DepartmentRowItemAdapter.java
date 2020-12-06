package com.ugur.safemealdeneme.Classes;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class DepartmentRowItemAdapter extends RecyclerView.Adapter<DepartmentRowItemAdapter.DepartmentRowItemHolder> {
    ArrayList<DepartmentRowItem> mDepartmentList;

    public static class DepartmentRowItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView textViewDepartmentName;
        public CardView cardView;

        public DepartmentRowItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewDepartmentName = itemView.findViewById(R.id.card_view_department_name);
            cardView = itemView.findViewById(R.id.departments_card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),11,0,"Change Name");
            menu.add(getAdapterPosition(),21,1,"Delete");
        }

    }

    public DepartmentRowItemAdapter(ArrayList<DepartmentRowItem> departmentList) {
        mDepartmentList = departmentList;
    }

    @NonNull
    @Override
    public DepartmentRowItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_department, parent,false);
        DepartmentRowItemHolder holder = new DepartmentRowItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentRowItemHolder holder, int position) {
        final DepartmentRowItem currentItem = mDepartmentList.get(position);
        holder.textViewDepartmentName.setText(currentItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Department Name: " + currentItem.getName() + "  ID: " + currentItem.getUuid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDepartmentList.size();
    }




}
