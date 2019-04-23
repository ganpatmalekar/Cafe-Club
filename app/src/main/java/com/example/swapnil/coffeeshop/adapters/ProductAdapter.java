package com.example.swapnil.coffeeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.activities.MenuItemDetails;
import com.example.swapnil.coffeeshop.models.MenuItemsModel;
import com.example.swapnil.coffeeshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private Context context;
    private List<MenuItemsModel> listItems;

    public ProductAdapter(Context context, List<MenuItemsModel> listItems){
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout, viewGroup, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder productHolder, int i) {
        final MenuItemsModel model = listItems.get(i);
        Picasso.with(context).load(model.getImage_url()).into(productHolder.imageView);
        productHolder.textView.setText(model.getProd_name());

        productHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = productHolder.getAdapterPosition();
                Intent intent = new Intent(context, MenuItemDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("prod_id", model.getProd_id());
                intent.putExtra("prod_name", model.getProd_name());
                intent.putExtra("prod_desc", model.getProd_desc());
                intent.putExtra("prod_price", model.getProd_price());
                intent.putExtra("prod_img", model.getImage_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.img);
            textView = (TextView)itemView.findViewById(R.id.name);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}
