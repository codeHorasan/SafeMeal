package com.ugur.safemealdeneme.Classes;
// Importing necessary libraries
import java.util.List;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cengalabs.flatui.views.FlatButton;
import com.squareup.picasso.Picasso;
import com.ugur.safemealdeneme.Classes.Product;
import com.ugur.safemealdeneme.R;

public class ProductAdapter extends BaseAdapter{
//Defining variables for process context, product magnitude and viewholder
    private Context context;
    private List<Product> productList;
    private Product product;
    private ViewHolder viewHolder;

    static class ViewHolder{//How the product will look in the menu with name,price and image.
        private TextView  tvPrice, tvName;
        private ImageView Productimg;
        private double price;
    }

    public ProductAdapter(Context context, List<Product> productList){// constructor collects variables context and productlist
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {// Gets magnitude of product list
        return productList.size();
    }

    @Override
    public Object getItem(int position) {// Gets position of selected item in List
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {// Long function will store a 64 bit signed integer referring to item position in productlist
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {// setting up the layout
        // Gets process context
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// if convert view is null(if view doesnt exist), create one
        if (convertView == null) {
            view = new View(context);
            view = inflater.inflate(R.layout.customlist_product, null);

        } else {// else leave as is
            view = (View) convertView;
        }

        product = productList.get(position);
// find particular names, prices and images to use according to ID
        viewHolder = new ViewHolder();
        viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        viewHolder.Productimg = (ImageView) view.findViewById(R.id.Productimg);

        viewHolder.price = product.getPrice();
// Drawing image
        try {
            Picasso.with(context)
                    .load(product.getImageList().get(0))
                    .resize(120, 120)
                    .centerCrop()
                    .into(viewHolder.Productimg);
        }catch (Exception e){

        }

        viewHolder.tvName.setText(product.getName());
        viewHolder.tvPrice.setText(viewHolder.price + " TL");

        view.setTag(viewHolder);//Stores data related to the view within the view(or layout) itself.

        return view;
    }
}