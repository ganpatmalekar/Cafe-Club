package com.example.swapnil.coffeeshop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_message, et_email, et_name;
    private Button btn_submit;
    private SessionManager sessionManager;
    private static String URL_SEND_FEEDBACK = HomeActivity.BASE_URL + "/PHPScripts/giveFeedback.php";
    private String name, email, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = (Toolbar)this.findViewById(R.id.toolbar_product);
        toolbar.setTitle("Feedback");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_message = (EditText) findViewById(R.id.message);

        sessionManager = new SessionManager(this);

        btn_submit = (Button)findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    private void sendFeedback() {
        name = et_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        message = et_message.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("message");

                    if (msg.equals("success")){
                        Toast.makeText(getApplicationContext(), "Send Success!!!", Toast.LENGTH_LONG).show();
                        et_name.setText("");
                        et_email.setText("");
                        et_message.setText("");
                        et_name.requestFocus();
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
                params.put("name", name);
                params.put("email", email);
                params.put("msg", message);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
