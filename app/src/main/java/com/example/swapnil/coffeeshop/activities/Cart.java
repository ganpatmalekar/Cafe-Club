package com.example.swapnil.coffeeshop.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.SessionManager;
import com.example.swapnil.coffeeshop.adapters.CartAdapter;
import com.example.swapnil.coffeeshop.models.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtTotalPrice;
    private Button btnPlace;
    private RecyclerView.Adapter adapter;
    private List<CartModel> listItems;

    private SessionManager sessionManager;
    private static String URL_VIEW_CART = HomeActivity.BASE_URL + "/PHPScripts/viewCart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar)this.findViewById(R.id.toolbar_cart);
        toolbar.setTitle("Your Cart");
        setSupportActionBar(toolbar);

        //setting up back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)this.findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        txtTotalPrice = (TextView)this.findViewById(R.id.total);
        btnPlace = (Button)this.findViewById(R.id.btnPlaceOrder);

        listItems = new ArrayList<>();
        viewCart();
    }

    //add products into the cart
    private void viewCart() {
        final ProgressDialog progressDialog = new ProgressDialog(Cart.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final int[] total = {0};

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = null;
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CartModel model = new CartModel(
                                jsonObject.getString("cart_id"),
                                jsonObject.getString("product_name"),
                                jsonObject.getString("product_price"),
                                jsonObject.getString("product_quantity")
                        );
                        listItems.add(model);
                        total[0] += (Integer.parseInt(jsonObject.getString("product_price"))) * (Integer.parseInt(jsonObject.getString("product_quantity")));
                    }
                    Locale locale = new Locale("en", "IN");
                    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                    txtTotalPrice.setText(format.format(total[0]));

                    adapter = new CartAdapter(getApplicationContext(), listItems);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    buildDialog(Cart.this).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Cart.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String user_id = user.get(SessionManager.NAME);

                map.put("uid", user_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue.add(stringRequest);
    }

    public AlertDialog.Builder buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Items in Cart");
        builder.setMessage("Your cart is empty! Please add some items");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
