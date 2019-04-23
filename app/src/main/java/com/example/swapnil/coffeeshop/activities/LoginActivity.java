package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button login, signup;
    EditText txtUser, txtPass;
    TextView forgot;
    public static final String LOG_URL = HomeActivity.BASE_URL + "/PHPScripts/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        txtUser = (EditText)this.findViewById(R.id.user);
        txtPass = (EditText)this.findViewById(R.id.pwd);

        login = (Button)this.findViewById(R.id.login);
        signup = (Button)this.findViewById(R.id.signup);

        forgot = (TextView)this.findViewById(R.id.forgotPWD);
    }

    public void signup(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void doLogin(View view) {
        final String username = txtUser.getText().toString().trim();
        String password = txtPass.getText().toString().trim();

        if (username.isEmpty()){
            txtUser.setError("Username required");
            txtUser.requestFocus();
            return;
        }
        if (password.isEmpty()){
            txtPass.setError("Password required");
            txtPass.requestFocus();
            return;
        }
        if (!username.isEmpty() && !password.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOG_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String msg = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        msg = jsonObject.getString("message");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");

                        if (msg.equals("Login Success")) {
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id").trim();
                                String name = object.getString("name").trim();
                                sessionManager.createSession(name, id);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    /*try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            msg = object.getString("message");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (msg.equals("Login Success")) {
                        sessionManager.createSession(username);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_LONG).show();
                    }*/
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
                    map.put("em", txtUser.getText().toString());
                    map.put("pwd", txtPass.getText().toString());

                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    public void newPSWOTP(View view)
    {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
}