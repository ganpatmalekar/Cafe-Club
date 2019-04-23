package com.example.swapnil.coffeeshop.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.fragments.AboutUs;
import com.example.swapnil.coffeeshop.fragments.ContactUs;
import com.example.swapnil.coffeeshop.fragments.HomeFragment;
import com.example.swapnil.coffeeshop.fragments.MyProfile;
import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.SessionManager;
import com.example.swapnil.coffeeshop.fragments.ViewOrder;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    SessionManager sessionManager;

    TextView headerText;

    public static String BASE_URL = "https://webswap.000webhostapp.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!isConnected(HomeActivity.this)) {
            buildDialog(HomeActivity.this).show();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.blank_fragment, homeFragment);
            fragmentTransaction.commit();
        }

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        String mName = user.get(sessionManager.NAME);

        toolbar = (android.support.v7.widget.Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setTitle("Coffee Shop");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)this.findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView = (NavigationView)this.findViewById(R.id.navigationView);

        View header = navigationView.getHeaderView(0);
        headerText = (TextView)header.findViewById(R.id.headerLogin);
        headerText.setText(mName);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home_menu:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.blank_fragment, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.view_order_menu:
                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = manager1.beginTransaction();
                        ViewOrder viewOrder = new ViewOrder();
                        transaction1.replace(R.id.blank_fragment, viewOrder);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                        break;
                    case R.id.profile_menu:
                        Intent intent = new Intent(HomeActivity.this, MyProfile.class);
                        startActivity(intent);
                        break;
                    case R.id.about_menu:
                        FragmentManager manager3 = getSupportFragmentManager();
                        FragmentTransaction transaction3 = manager3.beginTransaction();
                        AboutUs aboutUs = new AboutUs();
                        transaction3.replace(R.id.blank_fragment, aboutUs);
                        transaction3.addToBackStack(null);
                        transaction3.commit();
                        break;
                    case R.id.contact_menu:
                        FragmentManager manager4 = getSupportFragmentManager();
                        FragmentTransaction transaction4 = manager4.beginTransaction();
                        ContactUs contactUs = new ContactUs();
                        transaction4.replace(R.id.blank_fragment, contactUs);
                        transaction4.addToBackStack(null);
                        transaction4.commit();
                        break;
                    case R.id.view_cart_menu:
                        Intent intent1 = new Intent(getApplicationContext(), Cart.class);
                        startActivity(intent1);
                        break;
                    case R.id.logout_menu:
                        sessionManager.logoutUser();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //check network connection
    public boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check your internet connection and try again...!!!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    public void goLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.cart_menu:
                Intent intent = new Intent(getApplicationContext(), com.example.swapnil.coffeeshop.activities.Cart.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}
