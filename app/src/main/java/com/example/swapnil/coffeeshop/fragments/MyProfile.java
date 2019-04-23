package com.example.swapnil.coffeeshop.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.swapnil.coffeeshop.SessionManager;
import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    private CircleImageView circleImageView;
    private EditText c_name, c_email, c_phone, c_addr;
    private Button btn_upload_photo;
    SessionManager sessionManager;
    String getUserId;
    private Menu action;
    private Bitmap bitmap;

    private static String URL_GET_PROFILE = HomeActivity.BASE_URL + "/PHPScripts/showProfile.php";
    private static String URL_EDIT_PROFILE = HomeActivity.BASE_URL + "/PHPScripts/editProfile.php";
    private static String URL_UPLOAD_IMAGE = HomeActivity.BASE_URL + "/PHPScripts/uploadProfileImage.php";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        toolbar = (Toolbar)this.findViewById(R.id.toolbar_profile);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);

        //setting up back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_upload_photo = (Button)this.findViewById(R.id.btn_photo);
        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        circleImageView = (CircleImageView)this.findViewById(R.id.c_photo);

        c_name = (EditText)this.findViewById(R.id.c_name);
        c_email = (EditText)this.findViewById(R.id.c_email);
        c_phone = (EditText)this.findViewById(R.id.c_phone);
        c_addr = (EditText)this.findViewById(R.id.c_address);

        c_name.setEnabled(false);
        c_email.setEnabled(false);
        c_phone.setEnabled(false);
        c_addr.setEnabled(false);

        c_name.setFocusableInTouchMode(false);
        c_email.setFocusableInTouchMode(false);
        c_phone.setFocusableInTouchMode(false);
        c_addr.setFocusableInTouchMode(false);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();
        getUserId = user.get(sessionManager.NAME);
    }

    //getting user details from database
    private void getUserProfile(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String msg;

                try {
                    JSONObject object = new JSONObject(response);
                    msg = object.getString("message");
                    JSONArray jsonArray = object.getJSONArray("data");

                    if (msg.equals("success")) {
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);

                            String name = object1.getString("nm").trim();
                            String email = object1.getString("em").trim();
                            String phone = object1.getString("ph").trim();
                            String address = object1.getString("addr").trim();
                            String photo = object1.getString("photo").trim();
                            String url = "https://webswap.000webhostapp.com/PHPScripts/images/"+getUserId+".jpg";

                            c_name.setText(name);
                            c_email.setText(email);
                            c_phone.setText(phone);
                            c_addr.setText(address);
                            Picasso.with(getBaseContext())
                                    .load(url)
                                    .placeholder(R.drawable.profile_pic)
                                    .centerCrop()
                                    .resize(circleImageView.getMeasuredWidth(), circleImageView.getMeasuredHeight())
                                    .error(R.drawable.coffee_logo)
                                    .into(circleImageView);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", getUserId);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        action = menu;
        action.findItem(R.id.save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.edit:
                c_name.setFocusableInTouchMode(true);
                c_email.setFocusableInTouchMode(true);
                c_phone.setFocusableInTouchMode(true);
                c_addr.setFocusableInTouchMode(true);

                c_name.setEnabled(true);
                c_email.setEnabled(true);
                c_phone.setEnabled(true);
                c_addr.setEnabled(true);

                InputMethodManager methodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.showSoftInput(c_name, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.edit).setVisible(false);
                action.findItem(R.id.save).setVisible(true);

                return true;

            case R.id.save:
                saveProfile();
                action.findItem(R.id.edit).setVisible(true);
                action.findItem(R.id.save).setVisible(false);

                c_name.setFocusableInTouchMode(false);
                c_email.setFocusableInTouchMode(false);
                c_phone.setFocusableInTouchMode(false);
                c_addr.setFocusableInTouchMode(false);
                c_name.setFocusable(false);
                c_email.setFocusable(false);
                c_phone.setFocusable(false);
                c_addr.setFocusable(false);

                c_name.setEnabled(false);
                c_email.setEnabled(false);
                c_phone.setEnabled(false);
                c_addr.setEnabled(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //updating user
    private void saveProfile(){
        final String name = this.c_name.getText().toString().trim();
        final String email = this.c_email.getText().toString().trim();
        final String phone = this.c_phone.getText().toString().trim();
        final String addr = this.c_addr.getText().toString().trim();
        final String id = getUserId;

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Saving...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("message");

                    if (msg.equals("success")){
                        Toast.makeText(getBaseContext(), "Profile saved success!!!", Toast.LENGTH_LONG).show();
                        sessionManager.createSession(name, id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nm", name);
                map.put("em", email);
                map.put("ph", phone);
                map.put("addr", addr);
                map.put("id", id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //setting image to CircleImageView
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadPicture(getUserId);
        }
    }

    private void uploadPicture(final String id) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("message");

                    if (msg.equals("success")){
                        Toast.makeText(getBaseContext(), "Profile pic updated!!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("photo", getStringImage(bitmap));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //converting image to base64 string
    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] imageByteArray = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
}
