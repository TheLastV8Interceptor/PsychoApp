package com.example.psychoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {

    public static final String ERROR_RESTART_MESSAGE = "error_restart_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Intent intent = getIntent();
        String message = intent.getStringExtra(DataActivity.ERROR_MESSAGE);
        TextView textView = findViewById(R.id.activity_error_comm_TextView);
        textView.setText(message);
    }

    public void end_app(View view)
    {
        finishAffinity();
    }

    public void errorLaunchLoginActivity(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        String error_restart_message ="error_restart_message";
        intent.putExtra(ERROR_RESTART_MESSAGE, error_restart_message);
        startActivity(intent);
        this.finish();
    }

}