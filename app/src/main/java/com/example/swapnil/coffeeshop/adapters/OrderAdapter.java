package com.example.swapnil.coffeeshop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.models.OrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    Context context;
    List<OrderModel> list;

    public OrderAdapter(Context context, List<OrderModel> listItems){
        this.context = context;
        this.list = listItems;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, viewGroup, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder orderHolder, int i) {
        OrderModel model = list.get(i);

        Picasso.with(context).load(model.getProd_image()).into(orderHolder.imageView);
        orderHolder.textView.setText(model.getProd_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.imgViewProd);
            textView = (TextView)itemView.findViewById(R.id.txv_prod_name);
        }
    }
}
