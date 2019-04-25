package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.swapnil.coffeeshop.SendMail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView textViewLogin;
    private EditText etName, etEmail, etContact, etPassword, etConf_pass, etAddress;
    private Button btn_register;

    private static String REG_URL = HomeActivity.BASE_URL + "/PHPScripts/customer_register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText)findViewById(R.id.name);
        etEmail = (EditText)findViewById(R.id.email);
        etContact = (EditText)findViewById(R.id.phone);
        etPassword = (EditText)findViewById(R.id.password);
        etConf_pass = (EditText)findViewById(R.id.confirm_pass);
        etAddress = (EditText)findViewById(R.id.address);

        btn_register = (Button)findViewById(R.id.register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister(v);
            }
        });

        textViewLogin = (TextView)findViewById(R.id.txv_login);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //insert into employee table
    public void validateRegister(View view) {
        /*selected_id = gender.getCheckedRadioButtonId();
        radio = (RadioButton)this.findViewById(selected_id);
        gen = radio.getText().toString();*/
        final String nm = etName.getText().toString().trim();
        final String em = etEmail.getText().toString().trim();
        final String cn = etContact.getText().toString().trim();
        final String psw = etPassword.getText().toString().trim();
        final String c_psw = etConf_pass.getText().toString().trim();
        final String addrr = etAddress.getText().toString().trim();

        if (nm.isEmpty()){
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (em.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if (cn.isEmpty()){
            etContact.setError("Phone number required");
            etContact.requestFocus();
            return;
        }
        if (psw.isEmpty()){
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }
        if (psw.length() < 8){
            etPassword.setError("Password should be atleast 8 characters long");
            etPassword.requestFocus();
            return;
        }
        if (!psw.equals(c_psw)){
            etConf_pass.setError("Password don't match");
            etConf_pass.requestFocus();
            return;
        }
        if (addrr.isEmpty()){
            etAddress.setError("Address required");
            etAddress.requestFocus();
            return;
        }
        if (!nm.isEmpty() && !em.isEmpty() && !cn.isEmpty() && !psw.isEmpty() && !c_psw.isEmpty() && !addrr.isEmpty()) {
            addToRegister(view);
        }
    }

    public void addToRegister(View view) {
        final String name, email, contact, password, address;

        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        contact = etContact.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        address = etAddress.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_URL, new Response.Listener<String>() {
            String msg = null;
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        msg = jsonObject.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (msg.equalsIgnoreCase("success")) {
                    Toast.makeText(RegisterActivity.this, "Registration Success!!!", Toast.LENGTH_SHORT).show();
                    sendMail();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error: "+msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", contact);
                params.put("pass", password);
                params.put("address", address);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);
    }

    //Sending mail after successful registration
    public void sendMail() {
        String nm = etName.getText().toString();
        String receipant = etEmail.getText().toString().trim();
        String subject  = "Success";
        String message = "Dear "+nm+",\n\n\tThank you for registering with us.";

        //Creating SendMail Object
        SendMail sendMail = new SendMail(this, receipant, subject, message);

        //Executing SendMail to send mail
        sendMail.execute();
    }
}