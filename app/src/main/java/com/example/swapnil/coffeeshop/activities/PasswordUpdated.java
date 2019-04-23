package com.example.swapnil.coffeeshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.R;

public class PasswordUpdated extends AppCompatActivity {

    private TextView txtClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_updated);

        txtClick = (TextView)this.findViewById(R.id.click_here);
        txtClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordUpdated.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
