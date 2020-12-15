package com.example.psychoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    public static final String MAIN_MESSAGE = "com.example.android.psychoapp.main.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String message = intent.getStringExtra(DecisionActivity.DECISION_MESSAGE);
    }

    public void switch_to_login(View view)
    {
        launchLoginActivity(view);
    }

    public void launchLoginActivity(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        String main_message ="main_message";
        intent.putExtra(MAIN_MESSAGE, main_message);
        startActivity(intent);
        this.finish();
    }

}