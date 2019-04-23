package com.example.swapnil.coffeeshop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swapnil.coffeeshop.models.CartModel;
import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.example.swapnil.coffeeshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private Context context;
    private List<CartModel> listItems;
    private static String URL_DELETE_CART = HomeActivity.BASE_URL + "/PHPScripts/deleteFromCart.php";

    public CartAdapter(Context context, List<CartModel> listItems)
    {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, viewGroup, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder cartHolder, int i) {
        final CartModel cartModel = listItems.get(i);

        cartHolder.qty.setText("Quantity: "+ cartModel.getCart_item__qty());
        cartHolder.name.setText(cartModel.getCart_item_name());

        Locale locale = new Locale("en", "IN");
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(cartModel.getCart_item__price()));// * (Integer.parseInt(cartModel.getCart_item__qty()));
        cartHolder.price.setText("Price: "+format.format(price));

        cartHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = cartHolder.getAdapterPosition();
                deleteFromCart(pos);
            }
        });
    }

    private void deleteFromCart(final int pos) {
        final CartModel model = listItems.get(pos);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = null;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        msg = object.getString("message");
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                if (msg.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(context, "Product deleted from cart successfully...", Toast.LENGTH_LONG).show();
                    listItems.remove(pos);
                    notifyItemRemoved(pos);
                }
                else {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams()
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("cid", model.getCart_item_id());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        TextView name, price, qty;
        ImageView delete;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.cart_item_name);
            price = (TextView)itemView.findViewById(R.id.cart_item_price);
            qty = (TextView)itemView.findViewById(R.id.cart_item_qty);

            delete = (ImageView)itemView.findViewById(R.id.cart_item_delete);
        }
    }
}
