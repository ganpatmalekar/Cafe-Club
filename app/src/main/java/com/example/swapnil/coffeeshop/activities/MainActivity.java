package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.swapnil.coffeeshop.R;
import com.example.swapnil.coffeeshop.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private static int SPALSH_TIME_OUT = 2000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        progressBar = (ProgressBar)this.findViewById(R.id.progress);
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPALSH_TIME_OUT);
    }
}
