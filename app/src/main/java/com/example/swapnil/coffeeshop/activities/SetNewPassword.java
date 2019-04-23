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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetNewPassword extends AppCompatActivity {

    private TextView getUserEmail;
    private EditText txtOTP, newPWD, conf_newPWD;
    private Button updatePWD;
    private String getUserID;

    public  static  final String SET_NEWPSW_URL = HomeActivity.BASE_URL + "/PHPScripts/setNewPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        getUserEmail = (TextView)this.findViewById(R.id.txtGetUserEmail);

        txtOTP = (EditText)this.findViewById(R.id.txtOTP);
        newPWD = (EditText)this.findViewById(R.id.txtNewPwd);
        conf_newPWD = (EditText)this.findViewById(R.id.txtConfPwd);

        updatePWD = (Button)this.findViewById(R.id.setPWD);

        savedInstanceState = getIntent().getExtras();
        getUserID = savedInstanceState.getString("email");

        getUserEmail.setText(getUserID.toString().trim()+",");
    }

    public void setNewPSW(View view)
    {
        final String otp, password, conf_password;

        otp = txtOTP.getText().toString().trim();
        password = newPWD.getText().toString().trim();
        conf_password = conf_newPWD.getText().toString().trim();

        if (otp.isEmpty()){
            txtOTP.setError("OTP cannot be empty");
            txtOTP.requestFocus();
            return;
        }
        if (password.isEmpty()){
            newPWD.setError("Password cannot be empty");
            newPWD.requestFocus();
            return;
        }
        if (conf_password.isEmpty()){
            conf_newPWD.setError("Confirm Password cannot be empty");
            conf_newPWD.requestFocus();
            return;
        }
        if (!otp.isEmpty() && !password.isEmpty() && !conf_password.isEmpty()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, SET_NEWPSW_URL, new Response.Listener<String>() {

                String msg = null;
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            object.getString(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (msg.equals("success")) {
                        Toast.makeText(SetNewPassword.this, "Password Updated Success!!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SetNewPassword.this, PasswordUpdated.class);
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
                    map.put("email", getUserID);
                    map.put("pwd", password);
                    map.put("cnpass", conf_password);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
