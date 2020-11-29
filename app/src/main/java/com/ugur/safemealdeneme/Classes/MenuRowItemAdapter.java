package com.ugur.safemealdeneme.Classes;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

public class MenuRowItemAdapter extends RecyclerView.Adapter<MenuRowItemAdapter.MenuRowItemViewHolder> {
    ArrayList<MenuRowItem> mMenuList;

    public static class MenuRowItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView textViewMenuName;
        public CardView cardView;

        public MenuRowItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMenuName = itemView.findViewById(R.id.card_view_menu_name);
            cardView = itemView.findViewById(R.id.menus_card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),10,0,"Change Name");
            menu.add(getAdapterPosition(),20,1,"Delete");
        }

    }

    public MenuRowItemAdapter(ArrayList<MenuRowItem> menuList) {
        mMenuList = menuList;
    }

    @NonNull
    @Override
    public MenuRowItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_menu, parent,false);
        MenuRowItemViewHolder holder = new MenuRowItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuRowItemViewHolder holder, int position) {
        final MenuRowItem currentItem = mMenuList.get(position);
        holder.textViewMenuName.setText(currentItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Menu Name: " + currentItem.getName() + "  ID: " + currentItem.getUuid());
            }
        });

    }

    public void removeItem(int position) {
        String uuid = mMenuList.get(position).getUuid();
        mMenuList.remove(position);
        notifyDataSetChanged();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Companies").child(Company.getInstance().getUUID()).child("Menus").child(uuid).removeValue();
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }
}
