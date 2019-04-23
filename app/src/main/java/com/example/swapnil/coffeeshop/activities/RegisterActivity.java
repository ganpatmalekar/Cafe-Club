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

    TextView textViewLogin;
    EditText name, email, contact, pass, conf_pass, addr;
    /*RadioGroup gender;
    RadioButton radio;*/
    Button signup;

    private static String REG_URL = HomeActivity.BASE_URL + "/PHPScripts/customer_register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)this.findViewById(R.id.name);
        email = (EditText)this.findViewById(R.id.email);
        contact = (EditText)this.findViewById(R.id.phone);
        pass = (EditText)this.findViewById(R.id.password);
        conf_pass = (EditText)this.findViewById(R.id.confirm_pass);
        addr = (EditText)this.findViewById(R.id.address);

        //gender = (RadioGroup)this.findViewById(R.id.gender);

        signup = (Button)this.findViewById(R.id.register);

        textViewLogin = (TextView)this.findViewById(R.id.login);
    }

    //insert into employee table
    public void signup(View view)
    {
        /*selected_id = gender.getCheckedRadioButtonId();
        radio = (RadioButton)this.findViewById(selected_id);
        gen = radio.getText().toString();*/
        final String nm = this.name.getText().toString().trim();
        final String em = this.email.getText().toString().trim();
        final String cn = this.contact.getText().toString().trim();
        final String psw = this.pass.getText().toString().trim();
        final String c_psw = this.conf_pass.getText().toString().trim();
        final String addrr = this.addr.getText().toString().trim();

        if (nm.isEmpty()){
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if (em.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if (cn.isEmpty()){
            contact.setError("Phone number required");
            contact.requestFocus();
            return;
        }
        if (psw.isEmpty()){
            pass.setError("Password required");
            pass.requestFocus();
            return;
        }
        if (psw.length() < 8){
            pass.setError("Password should be atleast 8 characters long");
            pass.requestFocus();
            return;
        }
        if (!psw.equals(c_psw)){
            conf_pass.setError("Password don't match");
            conf_pass.requestFocus();
            return;
        }
        if (addrr.isEmpty()){
            addr.setError("Address required");
            addr.requestFocus();
            return;
        }
        if (!nm.isEmpty() && !em.isEmpty() && !cn.isEmpty() && !psw.isEmpty() && !c_psw.isEmpty() && !addrr.isEmpty()) {
            addToRegister();
        }
    }

    public void addToRegister()
    {
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String contact = this.contact.getText().toString().trim();
        final String password = this.pass.getText().toString().trim();
        final String c_password = this.conf_pass.getText().toString().trim();
        final String address = this.addr.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_URL, new Response.Listener<String>() {
            String msg = null;
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        msg = object.getString("message");
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                if (msg.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Registration success...", Toast.LENGTH_LONG).show();
                    sendMail();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", contact);
                params.put("pass", password);
                params.put("c_pass", c_password);
                params.put("address", address);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //Sending mail after successful registration
    public void sendMail()
    {
        String nm = name.getText().toString();
        String receipant = email.getText().toString().trim();
        String subject  = "Success";
        String message = "Dear "+nm+",\n\n\tThank you for registering with us.";

        //Creating SendMail Object
        SendMail sendMail = new SendMail(this, receipant, subject, message);

        //Executing SendMail to send mail
        sendMail.execute();
    }

    public void gotoLogin(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}