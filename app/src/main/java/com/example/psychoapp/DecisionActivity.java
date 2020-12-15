package com.example.psychoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DecisionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        Intent intent = getIntent();
        String message = intent.getStringExtra(DataActivity.DATA_MESSAGE);
    }

    public static final String DECISION_MESSAGE = "decision_message";

    public void decision_switch_to_login(View view)
    {
        decisionlaunchLoginActivity(view);
    }

    public void end_app(View view)
    {
        finishAffinity();
    }


    public void decisionlaunchLoginActivity(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        String decision_message ="decision_message";
        intent.putExtra(DECISION_MESSAGE, decision_message);
        startActivity(intent);
        this.finish();
    }


}