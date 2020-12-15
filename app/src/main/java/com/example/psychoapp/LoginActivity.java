package com.example.psychoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    protected EditText addressEditText;
    protected EditText portEditText;
    protected EditText loginEditText;
    protected EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        String message_from_main = intent.getStringExtra(MainActivity.MAIN_MESSAGE);
        String message_from_decision = intent.getStringExtra(DecisionActivity.DECISION_MESSAGE);
        String message_from_error = intent.getStringExtra(ErrorActivity.ERROR_RESTART_MESSAGE);
        addressEditText = findViewById(R.id.editTextServerName);
        portEditText = findViewById(R.id.editTextPortName);
        loginEditText = findViewById(R.id.editTextPasswordLogin);
        passwordEditText = findViewById(R.id.editTextPasswordPassword);
    }

    public static final String LOGIN_MESSAGE = "login_message";
    public static final String ADDRESS_MESSAGE = "address_message";
    public static final String PORT_MESSAGE = "port_message";
    public static final String MQTT_LOGIN_MESSAGE = "MQTT_login_message";
    public static final String MQTT_PASSWORD_MESSAGE = "MQTT_password_message";


    public void switch_to_data(View view)
    {
        launchDataActivity(view);
    }
    public void launchDataActivity(View view)
    {
        Intent intent = new Intent(this, DataActivity.class);
        String login_message ="login_message";
        intent.putExtra(LOGIN_MESSAGE, login_message);
        String addressmessage = addressEditText.getText().toString();
        String portmessage = portEditText.getText().toString();
        String loginmessage = loginEditText.getText().toString();
        String passwdmessage = passwordEditText.getText().toString();
        intent.putExtra(ADDRESS_MESSAGE, addressmessage);
        intent.putExtra(PORT_MESSAGE, portmessage);
        intent.putExtra(MQTT_LOGIN_MESSAGE, loginmessage);
        intent.putExtra(MQTT_PASSWORD_MESSAGE,passwdmessage);
        startActivity(intent);
        this.finish();
    }
}