package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.example.swapnil.coffeeshop.activities.SetNewPassword;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    private EditText txtEmail;
    private Button send;
    private String email;

    public static final String SEND_OTP_URL = HomeActivity.BASE_URL + "/PHPScripts/sendOTP.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtEmail = (EditText)this.findViewById(R.id.txtEmail);
        send = (Button)this.findViewById(R.id.sendotp);
    }

    public void sendOTP(View view) {
        email = txtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            txtEmail.setError("Email cannot be empty");
            txtEmail.requestFocus();
            return;
        }
        if (!email.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_OTP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String msg = null;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            msg = object.getString("message");
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (msg.equals("Email Matched")) {
                        //email = txtEmail.getText().toString().trim();

                        Toast.makeText(getApplicationContext(), "OTP send successfully...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("em", txtEmail.getText().toString());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
