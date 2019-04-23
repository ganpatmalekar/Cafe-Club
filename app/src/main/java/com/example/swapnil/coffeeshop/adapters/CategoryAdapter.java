package com.example.swapnil.coffeeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.models.CategoryModel;
import com.example.swapnil.coffeeshop.activities.ProductDetails;
import com.example.swapnil.coffeeshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private List<CategoryModel> category_list;

    public CategoryAdapter(Context context, List<CategoryModel> list){
        this.context = context;
        this.category_list = list;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_categories, viewGroup, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder categoryHolder, int i) {
        final CategoryModel model = category_list.get(i);
        Picasso.with(context).load(model.getCat_img_path()).into(categoryHolder.cat_img);
        categoryHolder.cat_name.setText(model.getCat_name());
        categoryHolder.cat_description.setText(model.getCat_desc());

        categoryHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = categoryHolder.getAdapterPosition();
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("cat_id", model.getCat_id());
                intent.putExtra("cat_name", model.getCat_name());
                context.startActivity(intent);
                /*HomeActivity homeActivity = (HomeActivity) v.getContext();

                switch (pos) {
                    case 0:
                        //displaying coffee items
                        Fragment coffeeMenus = new CoffeeMenus();
                        homeActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.blank_fragment, coffeeMenus)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 1:
                        //displaying tea items
                        Fragment teaMenus = new TeaMenus();
                        homeActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.blank_fragment, teaMenus)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 2:
                        //displaying shake items
                        Fragment shakeMenus = new ShakeMenus();
                        homeActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.blank_fragment, shakeMenus)
                                .addToBackStack(null)
                                .commit();
                        break;
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        private ImageView cat_img;
        private TextView cat_name, cat_description;
        private CardView cardView;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            cat_img = (ImageView)itemView.findViewById(R.id.cat_img);
            cat_name = (TextView)itemView.findViewById(R.id.cat_name);
            cat_description = (TextView)itemView.findViewById(R.id.cat_desc);

            cardView = (CardView)itemView.findViewById(R.id.card_menu_cat);
        }
    }
}
