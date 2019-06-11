package com.example.swapnil.coffeeshop.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.swapnil.coffeeshop.adapters.OrderAdapter;
import com.example.swapnil.coffeeshop.models.CartModel;
import com.example.swapnil.coffeeshop.models.OrderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Order extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<OrderModel> listItems;
    private SessionManager sessionManager;
    private static String URL_VIEW_ORDER = HomeActivity.BASE_URL + "/PHPScripts/viewOrder.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar = (Toolbar)this.findViewById(R.id.toolbar_order);
        toolbar.setTitle("Your Orders");
        setSupportActionBar(toolbar);

        //setting up back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)this.findViewById(R.id.order_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        listItems = new ArrayList<>();
        viewOrder();
    }

    //show orders
    private void viewOrder() {
        final ProgressDialog progressDialog = new ProgressDialog(Order.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final int[] total = {0};

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = null;
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrderModel model = new OrderModel(
                                jsonObject.getString("product_image"),
                                jsonObject.getString("product_name")
                        );
                        listItems.add(model);
                    }
                    adapter = new OrderAdapter(getApplicationContext(), listItems);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    buildDialog(Order.this, "Info", "Your have no recent orders.").show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Order.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Order.this);
        requestQueue.add(stringRequest);
    }

    public AlertDialog.Builder buildDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }
}
