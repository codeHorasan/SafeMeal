package com.ugur.safemealdeneme.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Activities.DepartmentActivity;
import com.ugur.safemealdeneme.Classes.Company;
import com.ugur.safemealdeneme.Models.PanelProductModel;
import com.ugur.safemealdeneme.R;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class PanelProductAdapter extends RecyclerView.Adapter<PanelProductAdapter.PanelProductViewHolder> {
    private ArrayList<PanelProductModel> mProductList;

    public static class PanelProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleText, descriptionText, priceText, tableText, dateHourText;
        public Button button;
        public RelativeLayout relativeLayout;
        public CardView cardView;

        public PanelProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.panel_image_view_product);
            button = itemView.findViewById(R.id.panel_button_done);
            titleText = itemView.findViewById(R.id.panel_text_view_product_title);
            descriptionText = itemView.findViewById(R.id.panel_text_view_product_description);
            priceText = itemView.findViewById(R.id.panel_text_view_product_price);
            tableText = itemView.findViewById(R.id.panel_table_text);
            dateHourText = itemView.findViewById(R.id.panel_date_hour_text);
            relativeLayout = itemView.findViewById(R.id.relative_layout_panel_product);
            cardView = itemView.findViewById(R.id.panel_card_view_product);
        }
    }

    public PanelProductAdapter(ArrayList<PanelProductModel> productList) {
        mProductList = productList;
    }

    @NonNull
    @Override
    public PanelProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_panel,parent,false);
        PanelProductViewHolder viewHolder = new PanelProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PanelProductViewHolder holder, int position) {
        final PanelProductModel currentItem = mProductList.get(position);
        holder.titleText.setText(currentItem.getTitle());
        holder.descriptionText.setText(currentItem.getDescription());
        holder.priceText.setText(currentItem.getPrice() + "$");
        holder.tableText.setText("Table " + currentItem.getTableNo());

        String dateString = currentItem.getDateString();
        String[] array = dateString.split("_");
        String yearMonthDay = array[0];
        String hourMinSec = array[1];
        String year = yearMonthDay.substring(0,4);
        String month = yearMonthDay.substring(4,6);
        String day = yearMonthDay.substring(6,8);
        String hour = hourMinSec.substring(0,2);
        String minute = hourMinSec.substring(2,4);

        holder.dateHourText.setText(month + "/" + day + "/" + year + "  --  " + hour + ":" + minute);

        Picasso.with(holder.imageView.getContext())
                .load(currentItem.getImageUri())
                .placeholder(R.drawable.border_selected)
                .fit().centerCrop()
                .transform(new BlurTransformation(holder.imageView.getContext(),6,1))
                .into(holder.imageView);

        if (currentItem.isDone()) {
            System.out.println("Done: " + currentItem.getTitle());
            holder.button.setBackgroundTintList(ContextCompat.getColorStateList(holder.button.getContext(),R.color.colorGray));
            holder.button.setClickable(false);
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#C1B7B7"));
            holder.cardView.setBackgroundColor(Color.parseColor("#C1B7B7"));
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.button.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(),R.color.colorGray));
                holder.button.setClickable(false);
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#C1B7B7"));
                holder.cardView.setBackgroundColor(Color.parseColor("#C1B7B7"));

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference("Customers");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String dbCompanyID = ds.child("companyID").getValue(String.class);
                            String dbDepartmentID =  ds.child("departmentID").getValue(String.class);
                            String dbMenuID = ds.child("menuID").getValue(String.class);
                            String dbTableNo = ds.child("tableNO").getValue(String.class);
                            String dbDateString = ds.child("dateString").getValue(String.class);

                            if (dbCompanyID.equals(Company.getInstance().getUUID()) && dbDepartmentID.equals(DepartmentActivity.departmentID)
                                    && dbMenuID.equals(DepartmentActivity.menuUUID) && dbTableNo.equals(currentItem.getTableNo())
                                    && dbDateString.equals(currentItem.getDateString())) {
                                for (DataSnapshot ds2 : ds.child("Orders").getChildren()) {
                                    if (currentItem.getID().equals(ds2.getKey())) {
                                        reference.child(ds.getKey()).child("Orders").child(ds2.getKey()).child("done").setValue("yes");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}
