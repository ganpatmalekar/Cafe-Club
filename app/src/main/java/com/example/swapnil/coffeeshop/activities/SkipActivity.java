package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.swapnil.coffeeshop.R;

public class SkipActivity extends AppCompatActivity {

    Button log, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);

        log = (Button)this.findViewById(R.id.logreg);
        next = (Button)this.findViewById(R.id.skip);
    }

    public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoNext(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
