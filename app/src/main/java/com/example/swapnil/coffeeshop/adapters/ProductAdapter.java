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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swapnil.coffeeshop.SessionManager;
import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.example.swapnil.coffeeshop.activities.MenuItemDetails;
import com.example.swapnil.coffeeshop.models.MenuItemsModel;
import com.example.swapnil.coffeeshop.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private Context context;
    private List<MenuItemsModel> listItems;
    private static String URL_ADD_FAV = HomeActivity.BASE_URL + "/PHPScripts/addFavourites.php";
    private SessionManager sessionManager;

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

        productHolder.imageViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = productHolder.getAdapterPosition();
                addToFavourite(pos);
                productHolder.imageViewFav.setImageResource(R.drawable.ic_favorite_add);
            }
        });
    }

    // add favourites
    private void addToFavourite(final int pos){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("message");

                    if (msg.equals("success")){
                        Toast.makeText(context, "Added to Favourite Success!!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                MenuItemsModel model = listItems.get(pos);
                sessionManager = new SessionManager(context);
                HashMap<String, String> user = sessionManager.getUserDetails();
                String user_id = user.get(SessionManager.NAME);

                Map<String, String> params = new HashMap<>();
                params.put("u_id", user_id);
                params.put("p_id", model.getProd_id());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imageViewFav;
        TextView textView;
        LinearLayout linearLayout;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.img);
            textView = (TextView)itemView.findViewById(R.id.name);
            imageViewFav = (ImageView)itemView.findViewById(R.id.fav_image);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}
