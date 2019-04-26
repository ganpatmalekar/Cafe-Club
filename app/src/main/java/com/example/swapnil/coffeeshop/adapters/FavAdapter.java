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
import com.example.swapnil.coffeeshop.models.FavModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder> {

    private Context context;
    private List<FavModel> listItems;

    public FavAdapter(Context context, List<FavModel> list) {
        this.context = context;
        this.listItems = list;
    }

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_layout, viewGroup, false);
        return new FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder favHolder, int i) {
        FavModel model = listItems.get(i);

        Picasso.with(context).load(model.getFav_item__url()).into(favHolder.image);
        favHolder.pname.setText(model.getFav_item_name());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class FavHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView pname;

        public FavHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.imgProd);
            pname = (TextView)itemView.findViewById(R.id.txv_prod_name);
        }
    }
}
