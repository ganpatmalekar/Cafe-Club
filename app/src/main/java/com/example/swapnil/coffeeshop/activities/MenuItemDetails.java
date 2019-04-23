package com.example.swapnil.coffeeshop.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuItemDetails extends AppCompatActivity {

    private TextView item_name, item_price, item_desc;
    private ImageView item_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnCart;
    private ElegantNumberButton numberButton;

    String prod_id, prod_name, prod_desc, prod_price, prod_image;
    private SessionManager sessionManager;
    public static String URL_ADD_TO_CART = HomeActivity.BASE_URL + "/PHPScripts/addToCart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        item_desc = (TextView)findViewById(R.id.item_description);
        item_name = (TextView)findViewById(R.id.item_name);
        item_price = (TextView)findViewById(R.id.item_price);
        item_image = (ImageView)findViewById(R.id.item_image);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        showItemDetails();

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    //show product details
    private void showItemDetails() {
        prod_id = getIntent().getExtras().getString("prod_id");
        prod_name = getIntent().getExtras().getString("prod_name");
        prod_desc = getIntent().getExtras().getString("prod_desc");
        prod_price = getIntent().getExtras().getString("prod_price");
        prod_image = getIntent().getExtras().getString("prod_img");

        collapsingToolbarLayout.setTitle(prod_name);
        item_name.setText(prod_name);
        item_desc.setText(prod_desc);

        Locale locale = new Locale("en", "IN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(prod_price));
        item_price.setText(format.format(price));

        //item_price.setText(prod_price+" Rs.");
        Picasso.with(this).load(prod_image).into(item_image);
    }

    //add products into the cart
    private void addToCart() {
        prod_id = getIntent().getExtras().getString("prod_id");
        prod_price = getIntent().getExtras().getString("prod_price");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("message");

                    if (msg.equals("success")){
                        Toast.makeText(getApplicationContext(), "Added to Cart Success!!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String user_id = user.get(SessionManager.NAME);

                Map<String, String> params = new HashMap<>();
                params.put("u_id", user_id);
                params.put("p_id", prod_id);
                params.put("p_qty", numberButton.getNumber());
                params.put("p_price", prod_price);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
