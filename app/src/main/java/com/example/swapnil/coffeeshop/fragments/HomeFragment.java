package com.example.swapnil.coffeeshop.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.example.swapnil.coffeeshop.adapters.CategoryAdapter;
import com.example.swapnil.coffeeshop.models.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageView image1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CategoryModel> listItems;
    public static String SHOW_CAT_URL = HomeActivity.BASE_URL + "/PHPScripts/showMenuCategory.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        image1 = (ImageView)view.findViewById(R.id.img1);

        recyclerView = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        listItems = new ArrayList<>();

        showCategory();
        return view;
    }

    //show category dynamically
    public void showCategory(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading menus...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_CAT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CategoryModel model = new CategoryModel(
                                object.getString("menu_id"),
                                object.getString("menu_names"),
                                object.getString("menu_description"),
                                object.getString("menu_img_path")
                        );
                        listItems.add(model);
                    }
                    adapter = new CategoryAdapter(getContext(), listItems);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
